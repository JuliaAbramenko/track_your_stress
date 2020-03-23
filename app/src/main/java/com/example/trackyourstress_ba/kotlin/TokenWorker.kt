package com.example.trackyourstress_ba.kotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.home.HomeActivity
import org.json.JSONObject
import kotlin.system.exitProcess

class TokenWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    var requestQueue: RequestQueue
    var currentContext: Context
    var sharedPreferences: SharedPreferences
    var notificationID = 1

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        currentContext = appContext
        sharedPreferences = currentContext.getSharedPreferences(
            currentContext.packageName, Context.MODE_PRIVATE
        )
    }

    override fun doWork(): Result {
        val oldToken = sharedPreferences.getString("token", "")!!
        refreshToken(oldToken)
        notificationID++
        return Result.success()
    }

    fun refreshToken(oldToken: String) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/refresh?token=$oldToken"
        val request = JsonObjectRequest(
            Request.Method.OPTIONS, url, null,
            Response.Listener {
                Log.w("token refresher", "you should never get here")
            }, Response.ErrorListener { error ->
                when {
                    error.networkResponse == null -> {
                        Log.w("token refresher", "options succeeded")
                        finishRefreshToken(oldToken)
                    }
                    error.networkResponse.statusCode == 400 -> {
                        sharedPreferences.edit().remove("token").commit()
                        Log.w("token refresher", "400 error on options")
                    }
                    error.networkResponse.statusCode == 409 -> {
                        sharedPreferences.edit().remove("token").commit()
                        Log.w("token refresher", "409 error on options")
                    }
                    else -> {
                        sharedPreferences.edit().remove("token").commit()
                        Log.w("token refresher", "unknown error on options")
                    }
                }
            })
        requestQueue.add(request)
    }

    private fun finishRefreshToken(oldToken: String) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/refresh?token=$oldToken"
        //{'observe':'response','headers':{'normalizedNames':{},'lazyUpdate':[{'name':'Content-Type','value':'application/json','op':'s'}],
        // 'headers':{},'lazyInit':{'normalizedNames':{},'lazyUpdate':'null','headers':{}}}}
        val json = JSONObject(
            mapOf(
                "observe" to "response",
                "headers" to
                        mapOf(
                            "normalizedNames" to emptyMap<String, String>(),
                            "lazyUpdate" to arrayOf(
                                mapOf(
                                    "name" to "Content-Type",
                                    "value" to "application/json",
                                    "op" to "s"
                                )
                            ), "headers" to emptyMap<String, String>(),
                            "lazyInit" to mapOf(
                                "normalizedNames" to emptyMap<String, String>(),
                                "lazyUpdate" to null, "headers" to emptyMap<String, String>()
                            )
                        )
            )
        )
        val request = JsonObjectRequest(
            Request.Method.POST, url, json,
            Response.Listener { response ->
                Log.w("token refresher", "token really refreshed")
                val newToken =
                    response.getJSONObject("data").getJSONObject("attributes").getString("token")
                sharedPreferences.edit().putString("token", newToken).apply()
                showNotification()
            }, Response.ErrorListener { error ->
                when {
                    error.networkResponse.statusCode == 400 -> {
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "400 error")
                        //ActivityCompat.finishAffinity(currentContext as HomeActivity)
                        exitProcess(0)
                    }
                    error.networkResponse.statusCode == 409 -> {
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "409 error")
                        //ActivityCompat.finishAffinity(currentContext as HomeActivity)
                        exitProcess(0)
                    }
                    else -> {
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "unknown error")
                        //ActivityCompat.finishAffinity(currentContext as HomeActivity)
                        exitProcess(0)
                    }
                }
            })
        requestQueue.add(request)
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(currentContext, "777")
            .setSmallIcon(R.drawable.ic_notifications_24dp)
            .setContentTitle("TrackYourStress")
            .setContentText("your token has been refreshed")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("your token has been refreshed")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "trackyourstress"
            val descriptionText = "strange channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("777", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                currentContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(notificationID, builder.build())
        } else {
            with(NotificationManagerCompat.from(currentContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationID, builder.build())
            }
        }
    }
}
