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
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.R
import org.json.JSONObject

class TokenReceiver : BroadcastReceiver() {
    private var requestQueue: RequestQueue
    lateinit var currentContext: Context
    lateinit var preferences: SharedPreferences

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        currentContext = context
        preferences = context.getSharedPreferences(
            context.packageName, Context.MODE_PRIVATE
        )
        val oldToken = preferences.getString("token", null)
        if (oldToken != null) {
            refreshToken(context, oldToken)
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
                if (error.networkResponse.statusCode == 400) {
                    preferences.edit().putString("token", null).apply()
                    tokenExpired()
                }
                if (error.networkResponse.statusCode == 409) {
                    tokenBlacklisted()
                    preferences.edit().putString("token", null).apply()
                }
            })
        requestQueue.add(request)
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

    }

    fun tokenBlacklisted() {

    }
}
