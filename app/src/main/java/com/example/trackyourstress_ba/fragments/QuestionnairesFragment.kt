package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.QuestionnaireUtils
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import org.json.JSONArray
import org.json.JSONObject


class QuestionnairesFragment : Fragment() {

    lateinit var questionnaireUtils: QuestionnaireUtils
    var hasLoaded: Boolean = false
    lateinit var associatedQuestionnaires: HashMap<Int, String>

    private lateinit var tableQuestionnaires: TableLayout
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_questionnaires, container, false)
        currentContext = container!!.context
        tableQuestionnaires = view!!.findViewById(R.id.questionnaire_table)
        sharedPreferences =
            currentContext.getSharedPreferences(currentContext.packageName, Context.MODE_PRIVATE)

        val firstRow = TableRow(activity)
        val textTitle = TextView(activity)
        val testRunning = TextView(activity)
        val testRepeatable = TextView(activity)
        firstRow.layoutParams =
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        // TODO firstRow.background = "00000ff"
        textTitle.text = getString(R.string.title_questionnaire)
        testRunning.text = getString(R.string.running)
        testRepeatable.text = getString(R.string.repeatable)
        firstRow.addView(textTitle)
        firstRow.addView(testRunning)
        firstRow.addView(testRepeatable)
        tableQuestionnaires.addView(firstRow, 0)
        return view
    }

    override fun onStart() {
        super.onStart()
        if (!hasLoaded) {
            associatedQuestionnaires = HashMap()
            questionnaireUtils = QuestionnaireUtils()
            questionnaireUtils.getMyQuestionnaires(this)
            hasLoaded = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasLoaded) {
            associatedQuestionnaires = HashMap()
            questionnaireUtils = QuestionnaireUtils()
            //TODO hasLoaded = true
        }

    }

    fun getRelevantValues(current: JSONObject) {
        if (current.getString("is_filled_out") == "false") {
            val name = current["name"].toString()
            val title = current["title"].toString()
            val runningString = current["is_active"].toString()
            val running: Boolean = runningString == "1"
            val repeatString = current["is_multiple"].toString()
            val repeat: Boolean = repeatString == "1"
            fillQuestionnaireRow(name, title, running, repeat)
        }
    }

    fun startAnswerSheetActivity(response: JSONObject, questionnaireID: Int) {
        val intent = Intent(currentContext, AnswerSheetActivity::class.java)
        intent.putExtra("response", response.toString())
        intent.putExtra("id", questionnaireID.toString())
        startActivity(intent)
    }

    private fun fillQuestionnaireRow(
        name: String,
        titleName: String,
        running: Boolean,
        repeat: Boolean
    ) {
        val newRow = TableRow(activity)
        val title = TextView(activity)
        val isRunning = TextView(activity)
        val isRepeatable = TextView(activity)
        title.text = titleName.substring(20)
        isRunning.text = if (running) "YES" else "NO"
        isRepeatable.text = if (repeat) "YES" else "NO"
        newRow.addView(title)
        newRow.addView(isRunning)
        newRow.addView(isRepeatable)
        newRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        tableQuestionnaires.addView(newRow)
        listenToClickEvents(newRow, name)
    }

    private fun listenToClickEvents(row: TableRow, name: String) {
        row.setOnClickListener {
            var index = -1
            for ((key, value) in associatedQuestionnaires) {
                if (value == name) {
                    index = key
                    break
                }
            }
            questionnaireUtils.getQuestionnaireStructure(index, this)
        }

    }

    fun allQuestionnairesReceived(response: JSONObject?) {
        var array: JSONArray = response!!.getJSONArray("data")
        var regQuestionaire: JSONObject? = null
        var regIndex = -1
        for (i in 0 until array.length()) {
            if (array.getJSONObject(i).getJSONObject("attributes").getString("name")
                    .contains("Demography")
            ) {
                regQuestionaire = array.getJSONObject(i)
                regIndex = i
            }
        }
        if (regQuestionaire != null && regQuestionaire.getJSONObject("attributes")
                .getString("is_filled_out") == "false"
        ) {
            array = JSONArray()
            array.put(regQuestionaire)
        } else if (regIndex != -1) {
            array.remove(regIndex)
        }
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            val questionnaireId = item.getString("id").toInt()
            val questionnaireName = item.getJSONObject("attributes").getString("name")
            associatedQuestionnaires[questionnaireId] = questionnaireName
            getRelevantValues(item.getJSONObject("attributes"))
        }
    }
}