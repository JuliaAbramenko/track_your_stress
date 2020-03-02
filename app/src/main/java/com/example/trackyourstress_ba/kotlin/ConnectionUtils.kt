package com.example.trackyourstress_ba.kotlin

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.register.RegisterActivity
import com.example.trackyourstress_ba.ui.register.RegistrationConfirmationActivity
import org.json.JSONObject
import android.content.Context.MODE_PRIVATE


class ConnectionUtils {
    var requestQueue: RequestQueue
    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
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
        val jsonObject = JSONObject(data)
        val request = JsonObjectRequest(
            Request.Method.POST, url,jsonObject,
            Response.Listener { response ->
                caller.loginResponseReceived(email, response)

            }, Response.ErrorListener{error ->
                if (error.networkResponse.statusCode == 401) {
                    caller.notify401()
                }
                if (error.networkResponse.statusCode == 403) {
                    caller.notify403()
                }
            })
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

    fun getProfileData(caller: LoginActivity) {
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/my/profile?token=" + GlobalVariables.localStorage["token"]
        val profile_request = JsonObjectRequest(
            Request.Method.GET, url, null,
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