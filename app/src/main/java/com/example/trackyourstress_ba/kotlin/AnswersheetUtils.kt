package com.example.trackyourstress_ba.kotlin

import android.os.Build
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.questionElements.*
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import org.json.JSONObject

open class AnswersheetUtils {
    private var requestQueue: RequestQueue

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
        val answerJSON = ArrayList<JSONObject>()
        guiList.forEach { item ->
            when (item) {
                is SingleAnswerElement -> {
                    answerJSON.add(
                        when (item) {
                            is SingleChoice -> JSONObject(
                                mapOf(
                                    "collected_at" to item.timestamp,
                                    "label" to item.label,
                                    "value" to if (item.selectedValue.toIntOrNull() == null)
                                        item.selectedValue else item.selectedValue.toInt()
                                )
                            )
                            is DateElement -> JSONObject(
                                mapOf(
                                    "collected_at" to item.timestamp,
                                    "label" to item.label,
                                    "value" to item.selectedValue
                                )
                            )
                            else -> JSONObject(
                                mapOf(
                                    "collected_at" to item.timestamp,
                                    "label" to item.label,
                                    "value" to item.selectedValue.toInt()
                                )
                            )
                        }
                    )
                }
                is MultiAnswerElement -> {
                    val valArray = ArrayList<String>()
                    item.selectedValues.forEachIndexed { i, value ->
                        valArray.add(value)
                    }
                    answerJSON.add(
                        JSONObject(
                            mapOf(
                                "collected_at" to item.timestamp,
                                "label" to item.label,
                                "value" to valArray
                            )
                        )
                    )
                }
            }
        }

        val clientDevice = Build.MANUFACTURER + Build.MODEL + Build.VERSION.RELEASE

        val release = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        val clientOS = "Android SDK: $sdkVersion ($release)"
        val clientName = "track-your-stress 1.0.0"
        val clientJSON =
            JSONObject(mapOf("device" to clientDevice, "name" to clientName, "os" to clientOS))
        val currentTimestamp = System.currentTimeMillis() / 1000L
        val language = caller.sharedPreferences.getString("currentLanguage", null)
        val jsonObject = JSONObject(
            mapOf(
                "data" to mapOf(
                    "type" to "answersheets", "attributes" to mapOf(
                        "answers" to answerJSON,
                        "client" to clientJSON,
                        "collected_at" to currentTimestamp,
                        "locale" to language
                    )
                )
            )
        )
        val apiEndpoint = caller.sharedPreferences.getString("apiEndpoint", null)
        val token = caller.sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/questionnaires/$questionnaireID/answersheets?token=$token"

        val request = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            Response.Listener {
                caller.submitSuccess()
            }, Response.ErrorListener { error ->
                if (error.networkResponse == null || error.networkResponse.statusCode == 422) {
                    caller.submitSuccess()
                } else {
                    Log.e("AnswersheetActivity", "Answersheet submitting failed")
                    caller.submitFail()
                }

            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = mutableMapOf<String, String>()
                header["Accept-language"] = "de"
                return header
            }
        }
        print(jsonObject)
        requestQueue.add(request)

    }
}