package com.example.trackyourstress_ba.kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import org.json.JSONObject

class TokenReceiver : BroadcastReceiver() {
    lateinit var refresher: Refresher
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        currentContext = context!!
        sharedPreferences = context!!.getSharedPreferences(
            context!!.packageName, Context.MODE_PRIVATE
        )
        refresher = Refresher(sharedPreferences, this)
        val oldToken = sharedPreferences.getString("token", "")
        refresher.refreshToken(oldToken!!)
        /*val vibrator : Vibrator = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val vibratePattern = longArrayOf(0, 200, 200, 200)
            vibrator.vibrate(vibratePattern, -1)
        }*/
    }
}

class Refresher(preferences: SharedPreferences, caller: TokenReceiver) {
    var requestQueue: RequestQueue
    var currentContext: Context
    var sharedPreferences: SharedPreferences

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        sharedPreferences = preferences
        currentContext = caller.currentContext

    }

    fun refreshToken(oldToken: String) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/refresh?token=$oldToken"
        val request = JsonObjectRequest(
            Request.Method.POST, url, null,
            Response.Listener { response ->
                //notifyRefresh(response, currentContext)
                Log.w("token refresher", "Done")
            }, Response.ErrorListener { error ->
                if (error.networkResponse.statusCode == 400) {
                    sharedPreferences.edit().putString("token", "").apply()
                    Log.w("token refresher", "400 error")
                }
                if (error.networkResponse.statusCode == 409) {
                    sharedPreferences.edit().putString("token", "").apply()
                    Log.w("token refresher", "409 error")
                }
            })
        requestQueue.add(request)


    }

    private fun notifyRefresh(response: JSONObject, currentContext: Context) {
        val newToken =
            response.getJSONObject("data").getJSONObject("attributes").getString("token")
        sharedPreferences.edit().putString("token", newToken).apply()
        Toast.makeText(
            currentContext,
            "Token refreshed",
            Toast.LENGTH_LONG
        ).show()

    }
}