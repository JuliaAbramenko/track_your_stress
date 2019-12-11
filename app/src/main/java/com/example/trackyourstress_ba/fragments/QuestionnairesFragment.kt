package com.example.trackyourstress_ba.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject

class QuestionnairesFragment : Fragment() {

    var current_questionnaire_id = 1
    lateinit var questionnaire_ids: IntArray
    lateinit var associated_questionnaire_ids: IntArray

    private lateinit var table_questionnaires: TableLayout
    val columns = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_study_overview, container, false)
        table_questionnaires = view!!.findViewById(R.id.study_table)
        val first_row = TableRow(activity)
        val test_title = TextView(activity)
        val test_running = TextView(activity)
        val test_repeatable = TextView(activity)
        first_row.layoutParams =
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        test_title.text = getString(R.string.title_study)
        test_running.text = getString(R.string.running)
        test_repeatable.text = getString(R.string.repeatable)
        first_row.addView(test_title)
        first_row.addView(test_repeatable)
        first_row.addView(test_running)
        table_questionnaires.addView(first_row)

        return view
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