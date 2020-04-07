package com.example.trackyourstress_ba.kotlin

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.register.RegistrationActivity
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
        val apiEndpoint = caller.sharedPreferences.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/login"
        val jsonObject = JSONObject(
            mapOf(
                "data" to mapOf(
                    "type" to "users",
                    "attributes" to mapOf(
                        "email" to email,
                        "password" to password
                    )
                )
            )
        )
        val request = JsonObjectRequest(
            Request.Method.POST, url,jsonObject,
            Response.Listener { response ->
                caller.loginResponseReceived(email, response)
            }, Response.ErrorListener{error ->
                when {
                    error.networkResponse == null -> caller.notifyNetworkError()
                    error.networkResponse.statusCode == 401 -> caller.notify401()
                    error.networkResponse.statusCode == 403 -> caller.notify403()
                    else -> caller.notifyNetworkError()
                }
            })
        requestQueue.add(request)
    }

    fun getProfileData(caller: LoginActivity) {
        val apiEndpoint = caller.sharedPreferences.getString("apiEndpoint", null)
        val token = caller.sharedPreferences.getString("token", null)
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
        caller: RegistrationActivity
    ) {
        val jsonObject = JSONObject(
            mapOf(
                "data" to mapOf(
                    "type" to "users",
                    "attributes" to mapOf(
                        "email" to email.trim(),
                        "password" to password,
                        "password_confirmation" to password_confirmation,
                        "name" to username.trim()
                    )
                )
            )
        )
        val sharedPrefs = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val apiEndpoint = sharedPrefs.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/register"
        val request = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
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
            }, Response.ErrorListener {
                caller.nextStep()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = mutableMapOf<String, String>()
                header["Accept-language"] = "de"
                return header
            }
        }
        requestQueue.add(request)
    }

    fun resendVerificationLink(email: String, caller: RegistrationConfirmationActivity) {
        val sharedPrefs = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val apiEndpoint = sharedPrefs.getString("apiEndpoint", null)
        val jsonObject = JSONObject(
            mapOf(
                "type" to "users",
                "attributes" to mapOf(
                    "email" to email
                )
            )
        )
        val url = "$apiEndpoint/api/v1/verify/resend"
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener {
                caller.linkResent()
            }, Response.ErrorListener {
                caller.notify400()
            })
        requestQueue.add(request)
    }

    fun logoutUser(caller: HomeActivity) {
        val sharedPreferences = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val token = sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/auth/logout?token=$token"
        val request = StringRequest(
            Request.Method.DELETE, url,
            Response.Listener<String> {
                sharedPreferences.edit().remove("token").apply()

            }, Response.ErrorListener {
                caller.notify500()
            })

        requestQueue.add(request)
    }

}