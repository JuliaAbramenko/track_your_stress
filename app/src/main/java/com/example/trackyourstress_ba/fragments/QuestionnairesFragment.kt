package com.example.trackyourstress_ba.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject

class QuestionnairesFragment : Fragment() {

    var current_study_id = 1
    var current_questionnaire_id = 1
    lateinit var study_ids: IntArray
    lateinit var associated_questionnaire_ids: IntArray

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_questionnaires, container, false)
    }

    //TODO ITERATE OVER IDS
    fun studies_received(response: JSONObject) {
        /*for(i in 0 until response.getJSONObject("id").length()) {
            if() {
            }
        }
        current_study_id = response.getJSONObject("data").getJSONObject("id").toString().toInt()
*/
    }

    fun associated_questionnaires_received(response: JSONObject) {

    }

    fun associated_questionnaires_structure_received(response: JSONObject) {

    }

    fun questionnaire_received(response: JSONObject) {

    }

    fun questionnaire_structure_received(response: JSONObject) {

    }
}