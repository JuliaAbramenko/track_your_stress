package com.example.trackyourstress_ba.kotlin

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
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

    private fun refreshToken(oldToken: String) {
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
                        ClearingUtils.logoutUser(currentContext)
                        Log.w("token refresher", "400 error on options")
                    }
                    error.networkResponse.statusCode == 409 -> {
                        ClearingUtils.logoutUser(currentContext)
                        Log.w("token refresher", "409 error on options")
                    }
                    else -> {
                        ClearingUtils.logoutUser(currentContext)
                        Log.w("token refresher", "unknown error on options")
                    }
                }
            })
        requestQueue.add(request)
    }

    private fun finishRefreshToken(oldToken: String) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/refresh?token=$oldToken"
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
                                "normalizedNames" to emptyMap(),
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
            }, Response.ErrorListener { error ->
                when {
                    error.networkResponse == null -> {
                        ClearingUtils.logoutUser(currentContext)
                        ActivityCompat.finishAffinity(currentContext as HomeActivity)
                    }
                    error.networkResponse.statusCode == 400 -> {
                        Log.w("token refresher", "400 error")
                        ClearingUtils.logoutUser(currentContext)
                        ActivityCompat.finishAffinity(currentContext as HomeActivity)
                    }
                    error.networkResponse.statusCode == 409 -> {
                        Log.w("token refresher", "409 error")
                        ClearingUtils.logoutUser(currentContext)
                        ActivityCompat.finishAffinity(currentContext as HomeActivity)
                    }
                    else -> {
                        Log.w("token refresher", "unknown error")
                        ClearingUtils.logoutUser(currentContext)
                        ActivityCompat.finishAffinity(currentContext as HomeActivity)
                    }
                }
            })
        requestQueue.add(request)
    }
}
