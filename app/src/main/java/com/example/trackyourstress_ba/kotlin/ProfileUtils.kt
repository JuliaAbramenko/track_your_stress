package com.example.trackyourstress_ba.kotlin

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.fragments.ProfileFragment
import org.json.JSONObject

class ProfileUtils() {
    var requestQueue: RequestQueue
    init {
        // Instantiate the cache
        val cache = NoCache() //TODO diskbased cache
        // Set up the network to use HttpURLConnection as the HTTP client
        val network: BasicNetwork = BasicNetwork(HurlStack())
        // Instantiate the RequestQueue with the cache and network. Start the queue
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    fun getProfile(caller : ProfileFragment) {
        val token = GlobalVariables.localStorage["token"]
        val url = GlobalVariables.apiEndPoint + "/api/v1/my/profile?token=" + token
        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                //val token = response.getJSONObject("data").getJSONObject("attributes").getString("token")
                //GlobalVariables.localStorage["token"] = token
                caller.response_received(response)
            }, Response.ErrorListener{ error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)
    }
}