package com.example.trackyourstress_ba.kotlin

import android.os.Build
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.QuestionElements.AnswerElement
import com.example.trackyourstress_ba.QuestionElements.MultiAnswerElement
import com.example.trackyourstress_ba.QuestionElements.SingleAnswerElement
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import org.json.JSONObject
import kotlin.reflect.typeOf

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

    fun submitAnswersheet(
        guiList: ArrayList<AnswerElement>,
        questionnaireID: Int,
        caller: AnswerSheetActivity
    ) {
        var answerJSON = "["

        //for(item in guiList) {
        guiList.forEachIndexed { j, item ->
            when (item) {
                is SingleAnswerElement -> {
                    answerJSON += "{\"collected_at\" : " + item.timestamp + ", \"label\": \"" + item.label + "\", \"value\" : " + item.selectedValue + "}"
                }
                is MultiAnswerElement -> {
                    answerJSON += "{\"collected_at\" : " + item.timestamp + ",  \"label\": \"" + item.label + "\", \"value\" : "
                    var valueString = "["
                    item.selectedValues.forEachIndexed { i, value ->
                        valueString += "\"" + value + "\""
                        if (item.selectedValues.lastIndex != i) {
                            valueString += ","
                        }
                    }
                    valueString += "]}"
                    answerJSON += valueString
                }
            }
            if (guiList.lastIndex != j && j != 0) {
                answerJSON += ","
            }
        }
        answerJSON += "]"


        val clientDevice = Build.MANUFACTURER + Build.MODEL + Build.VERSION.RELEASE

        val release = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        val clientOS = "Android SDK: $sdkVersion ($release)"
        val clientName = GlobalVariables.clientName
        val clientJSON =
            "{\"device\" : \"$clientDevice\", \"name\" : \"$clientName\", \"os\" : \"$clientOS\"}"
        val currentTimestamp = System.currentTimeMillis() / 1000L
        var data = "{\"data\" : {\"type\" : \"answersheets\", \"attributes\" : " +
                "{\"answers\" : $answerJSON, " +
                "\"client\" : $clientJSON, " +
                "\"collected_at\" : \"$currentTimestamp\", " +
                "\"locale\" : \"${GlobalVariables.cur_language}\"}}}"

        var id = questionnaireID
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/questionnaires/" + id + "/answersheets?token=" + GlobalVariables.localStorage["token"]
        var jsonObject = JSONObject(data)
        GlobalVariables.logger.info("msg sent:" + data)
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                caller.submitSuccess(response)
            }, Response.ErrorListener { error ->
                // Error in request
                GlobalVariables.logger.info("Error at server: " + error)
                caller.submitFail(error)

            })
        requestQueue.add(request)

    }
}