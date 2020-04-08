package com.example.trackyourstress_ba.kotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache
import com.android.volley.toolbox.StringRequest
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.StartActivity as StartActivity1

class ClearingUtils {

    companion object {
        private var requestQueue: RequestQueue

        init {
            val cache = NoCache()
            val network = BasicNetwork(HurlStack())
            requestQueue = RequestQueue(cache, network).apply {
                start()
            }
        }
        fun clearSharedPreferences(context: Context) {
            val sharedPreferences = context.getSharedPreferences(
                context.packageName, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().clear().apply()
        }

        fun logoutUser(context: Context) {
            val sharedPreferences = context.getSharedPreferences(
                context.packageName, Context.MODE_PRIVATE
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

                })

            requestQueue.add(request)
        }

        fun showLogout(context: Context) {
            Toast.makeText(
                context,
                context.getString(R.string.logout_successful),
                Toast.LENGTH_LONG
            ).show()
        }

        fun returnToLogin(context: Context) {
            val intent = Intent(context, StartActivity1::class.java)
            context.startActivity(intent)
        }
    }


}