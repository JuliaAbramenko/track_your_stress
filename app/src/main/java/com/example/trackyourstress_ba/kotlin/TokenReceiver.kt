package com.example.trackyourstress_ba.kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.json.JSONArray
import org.json.JSONObject
import kotlin.system.exitProcess

class TokenReceiver : BroadcastReceiver() {
    lateinit var refresher: Refresher
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        currentContext = context!!
        sharedPreferences = context.getSharedPreferences(
            context.packageName, Context.MODE_PRIVATE
        )
        refresher = Refresher(sharedPreferences, this)
        val oldToken = sharedPreferences.getString("token", "")
        refresher.refreshToken(oldToken!!)
        /*val vibrator : Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val vibratePattern = longArrayOf(0, 400, 100, 200)
            vibrator.vibrate(vibratePattern, -1)
        }*/
    }
}

class Refresher(preferences: SharedPreferences, caller: TokenReceiver) {
    var requestQueue: RequestQueue
    var currentContext: Context
    var sharedPreferences: SharedPreferences

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        sharedPreferences = preferences
        currentContext = caller.currentContext

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
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "400 error")
                    }
                    error.networkResponse.statusCode == 409 -> {
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "409 error")
                    }
                    else -> {
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "unknown error")
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
                notifyRefresh(response, currentContext)

            }, Response.ErrorListener { error ->
                when {
                    error.networkResponse.statusCode == 400 -> {
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "400 error")
                        finishAffinity(currentContext as HomeActivity)
                        exitProcess(0)
                    }
                    error.networkResponse.statusCode == 409 -> {
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "409 error")
                        finishAffinity(currentContext as HomeActivity)
                        exitProcess(0)
                    }
                    else -> {
                        sharedPreferences.edit().remove("token").apply()
                        Log.w("token refresher", "unknown error")
                        finishAffinity(currentContext as HomeActivity)
                        exitProcess(0)
                    }
                }
            })
        requestQueue.add(request)
    }

    private fun notifyRefresh(response: JSONObject, currentContext: Context) {
        val newToken =
            response.getJSONObject("data").getJSONObject("attributes").getString("token")
        sharedPreferences.edit().putString("token", newToken).apply()
        Toast.makeText(
            currentContext,
            "Token refreshed",
            Toast.LENGTH_LONG
        ).show()

    }
}