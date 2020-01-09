package com.example.trackyourstress_ba.kotlin

import android.os.Build
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache

class AnswersheetUtils {
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

    fun submitAnswersheet() {
        var clientDevice = Build.MANUFACTURER + Build.MODEL + Build.VERSION.RELEASE
        var clientOS = Build.VERSION.BASE_OS
        var clientName = GlobalVariables.clientName
        var clientJSON =
            "{'client' : { 'device' : '$clientDevice', 'name' : '$clientName', 'os' : '$clientOS'}}"
        var currentTimestamp = System.currentTimeMillis() / 1000L
        var answersJSON = "2"
        var data = " { 'data' : { 'type' : 'answersheets', 'attributes' : " +
                "{ 'answers' : '$answersJSON', " +
                "'client' : '$clientJSON', " +
                "'collected_at' : '$currentTimestamp', " +
                "'locale' : '${GlobalVariables.cur_language}'}}"
    }
}