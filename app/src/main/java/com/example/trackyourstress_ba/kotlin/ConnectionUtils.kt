package com.example.trackyourstress_ba.kotlin

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.register.RegisterActivity
import com.example.trackyourstress_ba.ui.register.RegistrationConfirmationActivity
import org.json.JSONObject


class ConnectionUtils {
    var requestQueue: RequestQueue
    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    fun loginUser(email: String, password: String, caller: LoginActivity) {
        val apiEndpoint = caller.preferences.getString("apiEndpoint", null)
        val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email', 'password' : '$password'}}}"
        val url = "$apiEndpoint/api/v1/auth/login"
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

    fun getProfileData(caller: LoginActivity) {
        val apiEndpoint = caller.preferences.getString("apiEndpoint", null)
        val token = caller.preferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/my/profile?token=$token"
        val profileRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                caller.profileResponseReceived(response)
            }, Response.ErrorListener {
                caller.notifyMissingData()
            })
        requestQueue.add(profileRequest)
    }

    fun registerUser(
        email: String,
        password: String,
        password_confirmation: String,
        username: String,
        caller: RegisterActivity
    ) {
        val data = "{ 'data' : {'type' : 'users', 'attributes' : {'email' : '$email', " +
                "'password' : '$password', 'password_confirmation' : '$password_confirmation', 'name' : '$username'}}}"
        val jsonObject = JSONObject(data)
        val sharedPrefs = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val apiEndpoint = sharedPrefs.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/register"
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener {
                val preferences = caller.getSharedPreferences(
                    caller.packageName, Context.MODE_PRIVATE
                )
                if (!preferences.contains("userEmail") || preferences.getString(
                        "userEmail",
                        null
                    ) != email
                ) {
                    preferences.edit().putString("userEmail", email).apply()
                }
                caller.registrationSuccessful()
            }, Response.ErrorListener {
                caller.registrationError()
            })
        requestQueue.add(request)
    }

    fun resendVerificationLink(email: String, caller: RegistrationConfirmationActivity) {
        val sharedPrefs = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val apiEndpoint = sharedPrefs.getString("apiEndpoint", null)
        val data = "data = { 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email'}}}"
        val url = "$apiEndpoint/api/v1/verify/resend"
        val jsonObject = JSONObject(data)
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                caller.linkResent()
            }, Response.ErrorListener { error ->
                caller.notify400()
            })
        requestQueue.add(request)
    }

    fun logoutUser(caller: HomeActivity) {
        val sharedPrefs = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val apiEndpoint = sharedPrefs.getString("apiEndpoint", null)
        val token = sharedPrefs.getString("token", null)
        val url = "$apiEndpoint/api/v1/auth/logout?token=$token"
        val request = StringRequest(
            Request.Method.DELETE, url,
            Response.Listener<String> { response: String ->


            }, Response.ErrorListener{error ->
                caller.notify500()
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
                caller.onNewTokenReceived(response)

            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("error occurred: $error")
            })
        requestQueue.add(request)
    }

}