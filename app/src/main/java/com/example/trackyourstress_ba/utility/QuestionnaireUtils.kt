package com.example.trackyourstress_ba.utility

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment

/**
 * Class used by the QuestionnairesFragment to make relevant API calls.
 * Uses a Volley RequestQueue to enqueue HTTP requests
 *
 */
class QuestionnaireUtils {
    private var requestQueue: RequestQueue

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    /**
     * Method for retrieving the raw JSON of an overview of the user's questionnaires. Values are used
     * later to be displayed in the QuestionnairesFragment.
     *
     * @param caller the QuestionnairesFragment. Used to invoke functions of that class to react
     * correspondingly to a server response.
     */
    fun getMyQuestionnaires(caller: QuestionnairesFragment) {
        val apiEndpoint = caller.sharedPreferences.getString("apiEndpoint", null)
        val token = caller.sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/my/questionnaires?token=$token"
        val request = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                caller.allQuestionnairesReceived(response)
            }, Response.ErrorListener { error ->
                if (error.networkResponse == null) {
                    Log.e("QuestionnaireFragment", "Network error occurred")
                    caller.notifyNetworkError()
                } else caller.notifyServerError()
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

    /**
     * Function for retrieving raw JSON for a specific questionnaire that is used to create parts of
     * the AnswerSheetActivity.
     *
     * @param questionnaireId The questionnaire with its unique id representation for which the
     * structure with questions and answers shall be fetched.
     * @param caller the QuestionnairesFragment. Used to invoke functions of that class to react
     * correspondingly to a server response.
     */
    fun getQuestionnaireStructure(questionnaireId: Int, caller: QuestionnairesFragment) {
        val apiEndpoint = caller.sharedPreferences.getString("apiEndpoint", null)
        val token = caller.sharedPreferences.getString("token", null)
        val url = "$apiEndpoint/api/v1/questionnaires/$questionnaireId/structure?token=$token"
        val request = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                caller.startAnswerSheetActivity(response, questionnaireId)
            }, Response.ErrorListener { error ->
                if (error.networkResponse == null) caller.notifyNetworkError()
                else caller.notifyServerError()
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