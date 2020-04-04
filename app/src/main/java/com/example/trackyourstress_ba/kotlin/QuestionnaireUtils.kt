package com.example.trackyourstress_ba.kotlin

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment

class QuestionnaireUtils {
    private var requestQueue: RequestQueue
    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    fun getMyQuestionnaires(caller: QuestionnairesFragment) {
        val apiEndpoint = caller.sharedPreferences.getString("apiEndpoint", null)
        val token = caller.sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/my/questionnaires?token=$token"
        val request = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                caller.allQuestionnairesReceived(response)
            }, Response.ErrorListener { error ->
                throw Exception("shit happened: $error")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = mutableMapOf<String, String>()
                val language = caller.sharedPreferences.getString("locale", "de")
                header["Accept-language"] = language!!
                return header
            }
        }

        requestQueue.add(request)
    }


    fun getQuestionnaireStructure(questionnaireId: Int, caller: QuestionnairesFragment) {
        val apiEndpoint = caller.sharedPreferences.getString("apiEndpoint", null)
        val token = caller.sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/questionnaires/$questionnaireId/structure?token=$token"
        val request = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                caller.startAnswerSheetActivity(response, questionnaireId)
            }, Response.ErrorListener { error ->
                throw Exception("shit happened: $error")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = mutableMapOf<String, String>()
                val language = caller.sharedPreferences.getString("locale", "de")
                header["Accept-language"] = language!!
                return header
            }
        }
        requestQueue.add(request)
    }
}