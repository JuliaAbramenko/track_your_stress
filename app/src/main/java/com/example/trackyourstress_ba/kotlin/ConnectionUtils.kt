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
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.register.RegisterActivity
import com.example.trackyourstress_ba.ui.register.RegistrationConfirmationActivity
import org.json.JSONObject

class ConnectionUtils() {
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


    fun registerUser(email: String, password: String, password_confirmation: String, username: String, caller: RegisterActivity) {
        val data = "{ 'data' : {'type' : 'users', 'attributes' : {'email' : '$email', " +
                "'password' : '$password', 'password_confirmation' : '$password_confirmation', 'name' : '$username'}}}"
        val jsonObject = JSONObject(data)
        val url = GlobalVariables.apiEndPoint + "/api/v1/auth/register"
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener{response ->
                caller.test_text.text = "registered"
                //dummy
                val x = response
            }, Response.ErrorListener{error ->
                // Error in request
                caller.test_text.text = "Already registered!"
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)
    }

    fun loginUser(email: String, password: String, caller: LoginActivity) {
        val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email', 'password' : '$password'}}}"
        val url = GlobalVariables.apiEndPoint + "/api/v1/auth/login"
        //val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/login", json=data)
        //val (request, response, result) = Fuel.post(GlobalVariables.apiEndPoint + "/api/v1/auth/login").body(data).response()
        val jsonObject = JSONObject(data)
        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.POST, url,jsonObject,
            Response.Listener { response ->
                val token = response.getJSONObject("data").getJSONObject("attributes").getString("token")
                GlobalVariables.localStorage["token"] = token
                GlobalVariables.localStorage["current_email"] = email
                caller.login_response_received(response)

            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)


    }

    fun logoutUser(caller: HomeActivity) {
        val url = GlobalVariables.apiEndPoint + "/api/v1/auth/logout?token=" + GlobalVariables.localStorage["token"].toString()
        val request = StringRequest(
            Request.Method.DELETE, url,
            Response.Listener<String> { response: String ->
                GlobalVariables.localStorage.remove("token")

            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("error occurred: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)
    }


    fun resendVerificationLink(email: String, caller: RegistrationConfirmationActivity) {
        val data = "data = { 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email'}}}"
        val url = GlobalVariables.apiEndPoint + "api/v1/verify/resend"
        val jsonObject = JSONObject(data)
        GlobalVariables.localStorage.put("current_email", email)
        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                Toast.makeText(caller,"we did it", LENGTH_SHORT).show()
                //dummy
                var x = 5
            }, Response.ErrorListener{error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)
    }

    fun refreshToken(caller: HomeActivity) {
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/auth/refresh?token=" + GlobalVariables.localStorage["token"]
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                //GlobalVariables.localStorage.remove("token")
                caller.on_new_token_received(response)

            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("error occurred: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(request)
    }

    fun get_profile_data(caller: LoginActivity) {
        val url_profile =
            GlobalVariables.apiEndPoint + "/api/v1/my/profile?token=" + GlobalVariables.localStorage["token"]
        val profile_request = JsonObjectRequest(
            Request.Method.GET, url_profile, null,
            Response.Listener { response ->
                caller.profile_response_received(response)
            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        requestQueue.add(profile_request)
    }

}