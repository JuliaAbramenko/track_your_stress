package com.example.trackyourstress_ba.kotlin

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.ui.login.LoginActivity
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


    fun loginUser(email: String, password: String, caller: LoginActivity) {
        val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email', 'password' : '$password'}}}"
        //val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/login", json=data)
        //val (request, response, result) = Fuel.post(GlobalVariables.apiEndPoint + "/api/v1/auth/login").body(data).response()
        val jsonObject = JSONObject(data)
        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.POST,GlobalVariables.apiEndPoint + "/api/v1/auth/login",jsonObject,
            Response.Listener { response ->
                val token = response.getJSONObject("data").getJSONObject("attributes").getString("token")
                caller.booltext.text = token
                GlobalVariables.localStorage["token"] = token
                //dummy
                var x = 5
            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)

    }

    fun logoutUser(token: String ) { //TODO ADD CALLER
        val data = "{}"
        val jsonObject = JSONObject(data)
        val url = GlobalVariables.apiEndPoint + "/api/v1/auth/logout?token=" + GlobalVariables.localStorage["token"].toString()
        val request = JsonObjectRequest(
            Request.Method.DELETE,url, jsonObject,
            Response.Listener { response ->
                GlobalVariables.localStorage.remove("token")
                //dummy
                val x = 5
            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
    }
}