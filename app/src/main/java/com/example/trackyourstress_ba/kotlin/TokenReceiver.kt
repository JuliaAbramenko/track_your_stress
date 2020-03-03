package com.example.trackyourstress_ba.kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import org.json.JSONObject

class TokenReceiver : BroadcastReceiver() {
    var requestQueue: RequestQueue

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val preferences = context.getSharedPreferences(
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
        val url =
            "$apiEndpoint/api/v1/auth/refresh?token=$oldToken"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                onNewTokenReceived(context, response)
            }, Response.ErrorListener { error ->
                if (error.networkResponse.statusCode == 400) {
                    tokenExpired()
                }
                if (error.networkResponse.statusCode == 409) {
                    tokenBlacklisted()
                }
            })
        requestQueue.add(request)
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
