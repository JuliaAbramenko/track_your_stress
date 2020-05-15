package com.example.trackyourstress_ba.Utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.fragments.ProfileFragment
import org.json.JSONObject

class ProfileUtils(caller: ProfileFragment) {
    private var requestQueue: RequestQueue
    var sharedPreferences: SharedPreferences = caller.currentContext.getSharedPreferences(
        caller.currentContext.packageName, Context.MODE_PRIVATE
    )

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    fun getProfile(caller : ProfileFragment) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val token = sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/my/profile?token=$token"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                caller.responseReceived(response, false)
            }, Response.ErrorListener{ error ->
                if (error.networkResponse == null) caller.notifyNetworkError()
                else caller.notifyServerError()
            })
        requestQueue.add(request)
    }

    fun updateProfile(
        username: String,
        firstName: String,
        lastName: String,
        sex: Int,
        caller: ProfileFragment,
        update: Boolean
    ) {
        val jsonObject = JSONObject(
            mapOf(
                "data" to mapOf(
                    "type" to "users",
                    "attributes" to mapOf(
                        "username" to username,
                        "firstname" to firstName,
                        "lastname" to lastName,
                        "sex" to sex
                    )
                )
            )
        )
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val token = sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/my/profile?token=$token"
        val request = JsonObjectRequest(
            Request.Method.PATCH, url, jsonObject,
            Response.Listener { response ->
                caller.responseReceived(response, update)
            }, Response.ErrorListener{error ->
                if (error.networkResponse == null) caller.notifyNetworkError()
                else caller.notifyServerError()
            })
        requestQueue.add(request)

    }

    fun updatePasswordOptions(newPassword: String, caller: ProfileFragment) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val token = sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/my/profile/password?token=$token"
        val request = JsonObjectRequest(
            Request.Method.OPTIONS, url, null,
            Response.Listener {
                Log.e("password options", "you should never get here")
            }, Response.ErrorListener { error ->
                if (error.networkResponse == null) caller.updatePasswordOptionsReceived(newPassword)
                else caller.notifyServerError()
            })
        requestQueue.add(request)
    }

    fun updatePassword(password: String, caller: ProfileFragment) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val token = sharedPreferences.getString("token", null)
        val jsonObject = JSONObject(
            mapOf(
                "data" to mapOf(
                    "type" to "users",
                    "attributes" to mapOf(
                        "password" to password,
                        "password_confirmation" to password
                    )
                )
            )
        )
        val url = "$apiEndpoint/api/v1/my/profile/password?token=$token"
        val request = JsonObjectRequest(
                Request.Method.PATCH, url, jsonObject,
            Response.Listener {
                Log.e("password updater", "with the current API state you should not get here")
            }, Response.ErrorListener{error ->
                if (error.networkResponse == null) {
                    Log.w("password updater", "success")
                    caller.updatePasswordReceived()
                } else caller.notifyServerError()

            })
        requestQueue.add(request)

    }

    fun repeatLogin(password: String, caller: ProfileFragment) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val email = sharedPreferences.getString("userEmail", null)
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
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                val newToken = response.getJSONObject("data").getJSONObject("attributes").getString("token")
                sharedPreferences.edit().putString("token", newToken).apply()
                caller.callNewPasswordDialog()
            }, Response.ErrorListener { error ->
                if (error.networkResponse.statusCode != 200) {
                    caller.callOldPasswordDialog()
                    caller.abortPasswordChange()
                }
            })
        requestQueue.add(request)
    }
}