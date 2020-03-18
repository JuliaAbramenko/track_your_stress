package com.example.trackyourstress_ba.kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.home.HomeActivity
import org.json.JSONObject

class TokenReceiver : BroadcastReceiver() {
    private var requestQueue: RequestQueue
    lateinit var currentContext: Context
    lateinit var preferences: SharedPreferences
    var connectionUtils: ConnectionUtils

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        connectionUtils = ConnectionUtils()
    }

    override fun onReceive(context: Context, intent: Intent) {
        currentContext = context
        preferences = context.getSharedPreferences(
            context.packageName, Context.MODE_PRIVATE
        )
        Log.e("TokenReceiver", "getting new token")
        val oldToken = preferences.getString("token", null)
        if (oldToken != null) {
            refreshToken(context, oldToken)
        } else {
            Log.e("TokenReceiver", "Token was null on refresh")
        }
    }

    fun refreshToken(context: Context, oldToken: String) {
        val preferences = context.getSharedPreferences(
            context.packageName, Context.MODE_PRIVATE
        )
        val apiEndpoint = preferences.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/refresh?token=$oldToken"
        val request = JsonObjectRequest(
            Request.Method.POST, url, null,
            Response.Listener { response ->
                showSuccess()
                onNewTokenReceived(context, response)
            }, Response.ErrorListener { error ->
                when {
                    error.networkResponse == null -> responseNull(error)
                    error.networkResponse.statusCode == 400 -> {
                        preferences.edit().putString("token", null).apply()
                        tokenExpired()
                    }
                    error.networkResponse.statusCode == 409 -> {
                        tokenBlacklisted()
                        preferences.edit().putString("token", null).apply()
                        tokenBlacklisted()
                    }
                    else -> something(error)
                }
                connectionUtils.logoutUser(context as HomeActivity)
            })
        requestQueue.add(request)
    }

    private fun responseNull(error: VolleyError?) {
        Log.e("token_network", "response is null")
    }

    private fun something(error: VolleyError) {
        Log.e("token_network", error.toString())
    }

    private fun showSuccess() {
        Log.w("Home activity", "token refreshed!")
    }

    fun onNewTokenReceived(context: Context, response: JSONObject) {
        val newToken =
            response.getJSONObject("data").getJSONObject("attributes").getString("token")
        val sharedPrefs = context.getSharedPreferences(
            context.packageName, Context.MODE_PRIVATE
        )
        sharedPrefs.edit().putString("token", newToken).apply()

    }

    fun tokenExpired() {
        Log.e("token_network", "Token expired")
    }

    fun tokenBlacklisted() {
        Log.e("token_network", "Token is already blacklisted")
    }
}
