package com.example.trackyourstress_ba.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.GlobalVariables
import com.example.trackyourstress_ba.kotlin.QuestionnaireUtils
import org.json.JSONArray
import org.json.JSONObject


class QuestionnairesFragment : Fragment() {

    var currentQuestionnaireID = 0
    var currentStudyID = 0
    lateinit var questionnaireUtils: QuestionnaireUtils
    //lateinit var questionnaire_ids: ArrayList<Int>
    //lateinit var associated_questionnaire_ids: ArrayList<Int>
    //lateinit var associatedQuestionnaireTitles: ArrayList<String>
    lateinit var associatedQuestionnaires: HashMap<Int, String>

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
        table_questionnaires.addView(first_row, 0)
        return view
    }

    override fun onStart() {
        super.onStart()
        questionnaireUtils = QuestionnaireUtils()
        associatedQuestionnaires = HashMap()
        questionnaireUtils.get_user_studies(GlobalVariables.localStorage["user_id"]!!.toInt(), this)
    }

    fun studies_received(response: JSONObject) {
        /*val tempArr= response.getJSONArray("data")
        for() {
           TODO
        }*/
        currentStudyID = 1
        questionnaireUtils.get_associated_questionnaires(currentStudyID, this)

    }

    fun associated_questionnaires_received(response: JSONObject) {
        GlobalVariables.logger.info("BAAAAAAANG!!!!")
        val array: JSONArray = response.getJSONArray("data")
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            val id = item["id"].toString().toInt()
            //item["name"]
            val title: String = item.getJSONObject("attributes")["name"].toString()
            associatedQuestionnaires[id] = title
        }

        requestQuestionnaires()
    }



    fun associated_questionnaires_structure_received(response: JSONObject) {

    }

    fun questionnaire_received(response: JSONObject) {
        GlobalVariables.logger.info("BOOOONG")
        val title =
            response.getJSONObject("data").getJSONObject("attributes")["title"].toString()
        val runningString =
            response.getJSONObject("data").getJSONObject("attributes")["is_active"].toString()
        val running: Boolean = runningString == "1"
        val repeatString =
            response.getJSONObject("data").getJSONObject("attributes")["is_multiple"].toString()
        val repeat: Boolean = repeatString == "1"
        fillQuestionnaireRow(title, running, repeat)
    }

    fun questionnaire_structure_received(response: JSONObject) {

    }

    private fun requestQuestionnaires() {
        for ((key, _) in associatedQuestionnaires) {
            questionnaireUtils.get_questionnaire(key, this)
        }
    }

    private fun fillQuestionnaireRow(name: String, running: Boolean, repeat: Boolean) {
        val newRow = TableRow(activity)
        val title = TextView(activity)
        val isRunning = TextView(activity)
        val isRepeatable = TextView(activity)
        title.text = name
        isRunning.text = if (running) "YES" else "NO"
        isRepeatable.text = if (repeat) "YES" else "NO"
        newRow.addView(title)
        newRow.addView(isRunning)
        newRow.addView(isRepeatable)
        newRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        table_questionnaires.addView(newRow)
    }
}