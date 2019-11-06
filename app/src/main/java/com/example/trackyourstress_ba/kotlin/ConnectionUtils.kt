package com.example.trackyourstress_ba.kotlin

import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import org.json.JSONObject

class ConnectionUtils() {
    var requestQueue: RequestQueue
    init {
        // Instantiate the cache
        val cache = NoCache() //TODO diskbased cache
        // Set up the network to use HttpURLConnection as the HTTP client.
        val network: BasicNetwork = BasicNetwork(HurlStack())
        // Instantiate the RequestQueue with the cache and network. Start the queue.
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }


    fun loginUser(email: String, password: String, booltext: TextView) {
        val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email', 'password' : '$password'}}}"
        //val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/login", json=data)
        //val (request, response, result) = Fuel.post(GlobalVariables.apiEndPoint + "/api/v1/auth/login").body(data).response()
        val jsonObject = JSONObject(data)
        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.POST,GlobalVariables.apiEndPoint + "/api/v1/auth/login",jsonObject,
            Response.Listener { response ->
                booltext.text = response.getJSONObject("data").getJSONObject("attributes").getString("token")
                GlobalVariables.localStorage["token"] = response.getString("data.attributes.token")
            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)

    }
}