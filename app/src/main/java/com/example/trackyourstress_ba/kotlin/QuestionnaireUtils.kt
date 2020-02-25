package com.example.trackyourstress_ba.kotlin

import android.provider.Settings
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment

class QuestionnaireUtils {

    val language_header = "{'Accept-language':" + GlobalVariables.cur_language + "}"
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


    fun get_user_studies(user_id: Int, caller: QuestionnairesFragment) {
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/users/" + user_id + "/studies/member?token=" + GlobalVariables.localStorage["token"]
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                GlobalVariables.logger.info("Received: $response.body")
                caller.studies_received(response)
            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        GlobalVariables.logger.info("Queued message: ${request.body}")
        requestQueue.add(request)
    }


    fun get_associated_questionnaires(study_id: Int, caller: QuestionnairesFragment) {
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/studies/" + study_id + "/questionnaires?token=" + GlobalVariables.localStorage["token"]
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                GlobalVariables.logger.info("Received: $response.body")
                caller.associated_questionnaires_received(response)
            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        GlobalVariables.logger.info("Queued message: ${request.body}")
        requestQueue.add(request)
    }

    /*fun get_associated_questionnaires_relationship(study_id: Int, caller: QuestionnairesFragment) {
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/studies/" + study_id + "/relationships/questionnaires?token=" + GlobalVariables.localStorage["token"]
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                GlobalVariables.logger.info("Received: $response.body")
                caller.associated_questionnaires_structure_received(response)
            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("shit happened: $error")
            })
        // Add the volley post request to the request queue
        GlobalVariables.logger.info("Queued message: ${request.body}")
        requestQueue.add(request)
    }*/

    fun get_questionnaire(questionnaire_id: Int, caller: QuestionnairesFragment) {
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/questionnaires/" + questionnaire_id + "?token=" + GlobalVariables.localStorage["token"]
        val request = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                GlobalVariables.logger.info("Received: $response.body")
                caller.questionnaire_received(response)
            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("shit happened: $error")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = mutableMapOf<String, String>()
                header.put("Accept-language", GlobalVariables.cur_language)
                return header
            }
        }

        // Add the volley post request to the request queue
        GlobalVariables.logger.info("Queued message: ${request.body}")
        requestQueue.add(request)
    }

    fun get_questionnaire_structure(questionnaire_id: Int, caller: QuestionnairesFragment) {
        val url =
            GlobalVariables.apiEndPoint + "/api/v1/questionnaires/" + questionnaire_id + "/structure?token=" + GlobalVariables.localStorage["token"]
        val request = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                GlobalVariables.logger.info("Received: $response.body")
                caller.questionnaire_structure_received(response, questionnaire_id)
            }, Response.ErrorListener { error ->
                // Error in request
                throw Exception("shit happened: $error")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = mutableMapOf<String, String>()
                header.put("Accept-language", GlobalVariables.cur_language)
                return header
            }
        }

        // Add the volley post request to the request queue
        GlobalVariables.logger.info("Queued message: ${request.body}")
        requestQueue.add(request)
    }
}