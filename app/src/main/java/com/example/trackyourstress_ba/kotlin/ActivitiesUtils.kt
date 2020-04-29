package com.example.trackyourstress_ba.kotlin

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.fragments.ActivitiesFragment

class ActivitiesUtils {
    private var requestQueue: RequestQueue

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    fun getActivities(page: Int, caller: ActivitiesFragment) {
        val sharedPrefs = caller.sharedPreferences
        val apiEndpoint = sharedPrefs.getString("apiEndpoint", null)
        val token = sharedPrefs.getString("token", null)
        val url = "$apiEndpoint/api/v1/my/activities?token=$token&page=$page"
        val request = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                caller.activitiesReceived(page, response)
            }, Response.ErrorListener { error ->
                if (error.networkResponse == null) {
                    Log.e("ActivitiesFragment", "Network error occurred")
                    caller.notifyNetworkError()
                }
                else caller.retrievalFailed()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val language = sharedPrefs.getString("locale", "de")
                val header = mutableMapOf<String, String>()
                header["Accept-language"] = language!!
                return header
            }
        }
        requestQueue.add(request)
    }
}