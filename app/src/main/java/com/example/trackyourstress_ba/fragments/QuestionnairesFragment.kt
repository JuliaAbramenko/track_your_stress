package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.QuestionnaireUtils
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import org.json.JSONArray
import org.json.JSONObject


class QuestionnairesFragment : Fragment() {

    lateinit var questionnaireUtils: QuestionnaireUtils
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
        this.activity?.title = getString(R.string.questionnaires)
        currentContext = container!!.context
        tableQuestionnaires = view!!.findViewById(R.id.questionnaire_table)
        sharedPreferences =
            currentContext.getSharedPreferences(currentContext.packageName, Context.MODE_PRIVATE)
        addFirstRow(currentContext)
        associatedQuestionnaires = HashMap()
        questionnaireUtils = QuestionnaireUtils()
        questionnaireUtils.getMyQuestionnaires(this)

        return view
    }

    private fun addFirstRow(currentContext: Context?) {
        val firstRow = TableRow(currentContext)
        val textTitle = TextView(currentContext)
        textTitle.background = resources.getDrawable(R.drawable.border)
        val testRunning = TextView(currentContext)
        testRunning.background = resources.getDrawable(R.drawable.border)
        val testRepeatable = TextView(currentContext)
        testRepeatable.background = resources.getDrawable(R.drawable.border)
        firstRow.layoutParams =
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        textTitle.text = getString(R.string.title_questionnaire)
        testRunning.text = getString(R.string.running)
        testRepeatable.text = getString(R.string.type)
        firstRow.addView(textTitle)
        firstRow.addView(testRunning)
        firstRow.addView(testRepeatable)
        tableQuestionnaires.addView(firstRow, 0)
    }


    private fun getRelevantValues(current: JSONObject) {
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
        val newRow = TableRow(currentContext)

        val title = TextView(currentContext)
        title.background = resources.getDrawable(R.drawable.border)
        val isRunning = TextView(currentContext)
        isRunning.background = resources.getDrawable(R.drawable.border)
        val isRepeatable = TextView(currentContext)
        isRepeatable.background = resources.getDrawable(R.drawable.border)
        title.text = titleName.substring(20)

        if (running) {
            isRunning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check24dp, 0, 0, 0)
        } else {
            isRunning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross24dp, 0, 0, 0)
        }
        if (repeat) {
            isRepeatable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_repeat24dp, 0, 0, 0)
        } else {
            isRepeatable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross24dp, 0, 0, 0)
        }
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

    fun notifyNetworkError() {
        Toast.makeText(
            currentContext,
            R.string.profile_not_loaded,
            Toast.LENGTH_LONG
        ).show()
    }

    fun notifyServerError() {
        Toast.makeText(
            currentContext,
            getString(R.string.server_error_occured),
            Toast.LENGTH_LONG
        ).show()
    }
}