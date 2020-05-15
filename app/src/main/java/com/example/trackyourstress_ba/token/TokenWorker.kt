package com.example.trackyourstress_ba.token

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.utility.ClearingUtils
import org.json.JSONObject

/**
 * Implementation of a Worker. Creates a TokenWorker that will perform its task periodically in the
 * HomeActivity.
 *
 * @constructor
 *
 * @param appContext the current context (information about application environment)
 * @param workerParams setup parameters for a ListenableWorker
 */
class TokenWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private var requestQueue: RequestQueue
    var currentContext: Context
    var sharedPreferences: SharedPreferences

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        currentContext = appContext
        sharedPreferences = currentContext.getSharedPreferences(
            currentContext.packageName, Context.MODE_PRIVATE
        )
    }

    /**
     * Actual task that is executed whenever the TokenWorker is triggered
     *
     * @return Result object that holds information whether the task succeeded
     */
    override fun doWork(): Result {
        val oldToken = sharedPreferences.getString("token", "")!!
        refreshToken(oldToken)
        return Result.success()
    }

    /**
     * Addition to the general onStopped() function. Whenever a token refresh cannot be executed, the user
     * is logged out and several values from the SharedPreferences are deleted.
     *
     */
    override fun onStopped() {
        super.onStopped()
        ClearingUtils.logoutUser(applicationContext)
    }

    /**
     * First step in the actual token refresh process: An OPTIONS request to the server.
     * On success finishRefreshToken is invoked.
     * On failure, the user is logged out by the ClearingUtils.
     *
     * @param oldToken essential parameter that is included into the URL for user identification.
     * Also used for the calculation of the new token
     */
    private fun refreshToken(oldToken: String) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/refresh?token=$oldToken"
        val request = JsonObjectRequest(
            Request.Method.OPTIONS, url, null,
            Response.Listener {
                Log.w("token refresher", "you should never get here")
            }, Response.ErrorListener { error ->
                when {
                    error.networkResponse == null -> {
                        Log.w("token refresher", "options succeeded")
                        finishRefreshToken(oldToken)
                    }
                    error.networkResponse.statusCode == 400 -> {
                        ClearingUtils.logoutUser(currentContext)
                        Log.e("token refresher", "400 error on options")
                    }
                    error.networkResponse.statusCode == 409 -> {
                        ClearingUtils.logoutUser(currentContext)
                        Log.e("token refresher", "409 error on options")
                    }
                    else -> {
                        ClearingUtils.logoutUser(currentContext)
                        Log.e("token refresher", "unknown error on options")
                    }
                }
            })
        requestQueue.add(request)
    }

    /**
     * Second step in the actual token refresher process: A POST request to the REST-API.
     * On success, a new token is stored into the SharedPreferences.
     * On failure, the user is logged out by the ClearingUtils.
     *
     * @param oldToken essential parameter that is included into the URL for user identification.
     * Also used for the calculation of the new token
     */
    private fun finishRefreshToken(oldToken: String) {
        val apiEndpoint = sharedPreferences.getString("apiEndpoint", null)
        val url = "$apiEndpoint/api/v1/auth/refresh?token=$oldToken"
        val json = JSONObject(
            mapOf(
                "observe" to "response",
                "headers" to
                        mapOf(
                            "normalizedNames" to emptyMap<String, String>(),
                            "lazyUpdate" to arrayOf(
                                mapOf(
                                    "name" to "Content-Type",
                                    "value" to "application/json",
                                    "op" to "s"
                                )
                            ), "headers" to emptyMap<String, String>(),
                            "lazyInit" to mapOf(
                                "normalizedNames" to emptyMap(),
                                "lazyUpdate" to null, "headers" to emptyMap<String, String>()
                            )
                        )
            )
        )
        val request = JsonObjectRequest(
            Request.Method.POST, url, json,
            Response.Listener { response ->
                Log.w("token refresher", "token refreshed successfully")
                val newToken =
                    response.getJSONObject("data").getJSONObject("attributes").getString("token")
                sharedPreferences.edit().putString("token", newToken).apply()
            }, Response.ErrorListener { error ->
                when {
                    error.networkResponse == null -> {
                        ClearingUtils.logoutUser(currentContext)
                        Log.e("token refresher", "no network connection")
                    }
                    error.networkResponse.statusCode == 400 -> {
                        Log.e("token refresher", "400 error")
                        ClearingUtils.logoutUser(currentContext)
                    }
                    error.networkResponse.statusCode == 409 -> {
                        Log.e("token refresher", "409 error")
                        ClearingUtils.logoutUser(currentContext)
                    }
                    else -> {
                        Log.e("token refresher", "unknown error")
                        ClearingUtils.logoutUser(currentContext)
                    }
                }
            })
        requestQueue.add(request)
    }
}
