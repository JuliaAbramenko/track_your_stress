package com.example.trackyourstress_ba.utility

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.registration.RegistrationActivity
import com.example.trackyourstress_ba.ui.registration.RegistrationConfirmationActivity
import org.json.JSONObject

/**
 * Class used by LoginActivity, RegistrationActivity, RegistrationConfirmationActivity and HomeActivity
 * to make relevant API calls. Uses a Volley RequestQueue to enqueue HTTP requests
 *
 */
class ConnectionUtils {
    private var requestQueue: RequestQueue
    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    /**
     * Performs a login with entered credentials to gain access to a token.
     *
     * @param email the user's email with which he has created a user account
     * @param password the user's password
     * @param caller the LoginActivity. Used to invoke functions from that class and react
     * correspondingly to the server response.
     */
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
            }, Response.ErrorListener{ error ->
                when {
                    error.networkResponse == null -> caller.notifyNetworkError()
                    error.networkResponse.statusCode == 401 -> caller.notify401()
                    error.networkResponse.statusCode == 403 -> caller.notify403()
                    else -> caller.notifyNetworkError()
                }
            })
        requestQueue.add(request)
    }

    /**
     * Second step after login. API call to retrieve userId and userName from the server to display
     * in the NavigationDrawer when forwarding to the HomeActivity.
     *
     * @param caller the LoginActivity. Used to invoke functions from that class and react
     * correspondingly to the server response.
     */
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

    /**
     * Used by the RegistrationActivity. API call to register a user to the platform.
     *
     * @param email the user's email that will be used
     * @param password  the user's password he came up with
     * @param password_confirmation the same password. Must match with the previous field
     * @param username the userName the user wishes to have
     * @param caller the RegistrationActivity. Used to invoke functions from that class and react
     * correspondingly to the server response.
     */
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
                    caller.nextStep()
                }
            }, Response.ErrorListener { error ->
                when {
                    error.networkResponse == null -> caller.noNetworkConnection()
                    error.networkResponse.statusCode == 422 -> caller.abort()
                    else -> caller.notifyError()
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = mutableMapOf<String, String>()
                header["Accept-language"] = "de"
                return header
            }
        }
        requestQueue.add(request)
    }

    /**
     * Function used by the RegistrationConfirmationActivity. Used to send another verification link
     * after registration to the previously entered email in the RegistrationActivity.
     *
     * @param email the email to which the link shall be resent
     * @param caller the RegistrationConfirmationActivity. Used to invoke functions from that
     * class and react correspondingly to the server response.
     */
    fun resendVerificationLink(email: String, caller: RegistrationConfirmationActivity) {
        Log.d("resend verification", "called")
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

    /**
     * Function used by the HomeActivity. A logout is performed when the LogoutFragment is selected.
     * The token is deleted from the SharedPreferences
     *
     * @param caller the HomeActivity. Used to invoke functions from that class and react
     * correspondingly to the server response.
     */
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
                sharedPreferences.edit().remove("token").apply()
                caller.notify500()
            })

        requestQueue.add(request)
    }

}