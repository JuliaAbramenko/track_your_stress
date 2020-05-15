package com.example.trackyourstress_ba.utility

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache
import com.android.volley.toolbox.StringRequest
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.start.StartActivity

/**
 * Class used to logout the user cleanly when errors occur.
 *
 */
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

        /**
         * Clears the user specific values "token", "userId" and "userName" from the SharedPreferences.
         * Notification settings remain.
         *
         * @param context information about the current activity
         */
        fun clearSharedPreferences(context: Context) {
            val sharedPreferences = context.getSharedPreferences(
                context.packageName, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().remove("token").apply()
            sharedPreferences.edit().remove("userId").apply()
            sharedPreferences.edit().remove("userName").apply()
        }


        /**
         * On error while refreshing the token, the user is logged out and the SharedPreferences are
         * adapted
         *
         * @param context information about the current activity
         */
        fun logoutUser(context: Context) {
            Log.e("ClearingUtils", "Sudden logout")
            val sharedPreferences = context.getSharedPreferences(
                context.packageName, Context.MODE_PRIVATE
            )
            val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
            val token = sharedPreferences.getString("token", null)
            val url = "$apiEndpoint/api/v1/auth/logout?token=$token"
            val request = StringRequest(
                Request.Method.DELETE, url,
                Response.Listener<String> {
                    clearSharedPreferences(
                        context
                    )
                }, Response.ErrorListener {
                    clearSharedPreferences(
                        context
                    )
                })
            requestQueue.add(request)
        }

        /**
         * Displayed Toast whenever a logout is performed correctly
         *
         * @param context information about the current environment
         */
        fun showLogout(context: Context) {
            Toast.makeText(
                context,
                context.getString(R.string.logout_successful),
                Toast.LENGTH_LONG
            ).show()
        }

        /**
         * Forced navigation back to the StartActivity when needed.
         *
         * @param context information about the current environment
         */
        fun returnToLogin(context: Context) {
            val intent = Intent(context, StartActivity::class.java)
            context.startActivity(intent)
        }
    }
}