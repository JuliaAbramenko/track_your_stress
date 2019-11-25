package com.example.trackyourstress_ba.kotlin

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
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

    fun updateProfile(username: String, firstname: String, lastname: String, sex: Int, caller: ProfileFragment) {
        val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'username' : '$username', 'firstname' : '$firstname', 'lastname' : '$lastname', 'sex' : '${sex.toString()}'}}}"
        val json = JSONObject(data)
        val token = GlobalVariables.localStorage["token"]
        val url = GlobalVariables.apiEndPoint + "/api/v1/my/profile?token=" + token
        val request = JsonObjectRequest(
            Request.Method.PATCH, url, json,
            Response.Listener { response ->
                caller.update_received(response)
            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("error occurred: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)

    }

    fun updataPassword(password: String, caller: ProfileFragment) {
        val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'password' : '$password'}}}"
        val json = JSONObject(data)
        val token = GlobalVariables.localStorage["token"]
        val url = GlobalVariables.apiEndPoint + "/api/v1/my/profile/password?token=" + token
        val request = JsonObjectRequest(
            Request.Method.PATCH, url, json,
            Response.Listener { response ->
                caller.update_password_received(response)
            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("error occurred: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)

    }

    fun deleteProfile(caller: ProfileFragment) {
        val token = GlobalVariables.localStorage["token"]
        val url = GlobalVariables.apiEndPoint + "/api/v1/my/profile?token=" + token
        val request = JsonObjectRequest(
            Request.Method.DELETE, url, null,
            Response.Listener { response ->
                caller.profile_deleted(response)
            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("error occurred: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)
    }
}