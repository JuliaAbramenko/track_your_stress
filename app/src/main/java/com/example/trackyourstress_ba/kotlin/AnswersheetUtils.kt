package com.example.trackyourstress_ba.kotlin

import android.os.Build
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.QuestionElements.*
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import org.json.JSONObject
import kotlin.reflect.typeOf

class AnswersheetUtils {
    var requestQueue: RequestQueue

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
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
                    answerJSON += if (item is SingleChoice) {
                        "{\"collected_at\" : " + item.timestamp + ", \"label\": \"" + item.label + "\", \"value\" : \"" + item.selectedValue + "\"}"
                    } else {
                        "{\"collected_at\" : " + item.timestamp + ", \"label\": \"" + item.label + "\", \"value\" : " + item.selectedValue + "}"
                    }
                }
                is MultiAnswerElement -> {
                    answerJSON += "{\"collected_at\" : " + item.timestamp + ", \"label\": \"" + item.label + "\", \"value\" : "
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
            if (j != guiList.lastIndex && j != 0 && item !is Text) {
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
        val apiEndpoint = caller.sharedPreferences.getString("apiEndpoint", null)
        val token = caller.sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/questionnaires/$questionnaireID/answersheets?token=$token"
        var jsonObject = JSONObject(data)
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                caller.submitSuccess(response)
            }, Response.ErrorListener { error ->
                caller.submitFail(error)
            })
        requestQueue.add(request)

    }
}