package com.example.trackyourstress_ba.kotlin

import android.app.PendingIntent.getActivities
import android.app.PendingIntent.getActivity
import android.provider.Settings
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.fragments.ActivitiesFragment
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.register.RegisterActivity
import com.example.trackyourstress_ba.ui.register.RegistrationConfirmationActivity
import org.json.JSONObject

class HomeUtils() {
    var requestQueue: RequestQueue

    init {
        // Instantiate the cache
        val cache = NoCache() //TODO diskbased cache
        // Set up the network to use HttpURLConnection as the HTTP client
        val network = BasicNetwork(HurlStack())
        // Instantiate the RequestQueue with the cache and network. Start the queue
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    fun getActivities(caller: ActivitiesFragment) {
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/my/activities?token=" + GlobalVariables.localStorage["token"]
        val request = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                //GlobalVariables.localStorage.remove("token")
                caller.activitiesReceived(response)

            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("error occurred: $error")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = mutableMapOf<String, String>()
                header["Accept-language"] = GlobalVariables.cur_language
                return header
            }
        }
        requestQueue.add(request)
    }

}