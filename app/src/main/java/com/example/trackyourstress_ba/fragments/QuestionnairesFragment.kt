package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.utility.QuestionnaireUtils
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.sqrt

/**
 * The class managing the QuestionnairesFragment in the navigation drawer
 */
class QuestionnairesFragment : Fragment() {

    private lateinit var questionnaireUtils: QuestionnaireUtils
    private lateinit var associatedQuestionnaires: HashMap<Int, String>
    private lateinit var tableQuestionnaires: TableLayout
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    /**
     * general creation method for the QuestionnairesFragment. Is called before it is displayed.
     */
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
        addFirstRow(currentContext)
        associatedQuestionnaires = HashMap()
        questionnaireUtils = QuestionnaireUtils()
        questionnaireUtils.getMyQuestionnaires(this)
        return view
    }

    override fun onStart() {
        super.onStart()
        val activity = this.activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.questionnaires)
    }

    /**
     * Method to add add the title row to the main View.
     *
     * @param currentContext: context used in this class extracted from the fragment container
     */
    private fun addFirstRow(currentContext: Context?) {
        val firstRow = TableRow(currentContext)
        val textTitle = TextView(currentContext)
        textTitle.background = resources.getDrawable(R.drawable.border)
        textTitle.setTypeface(null, Typeface.BOLD)
        val testRunning = TextView(currentContext)
        testRunning.background = resources.getDrawable(R.drawable.border)
        testRunning.setTypeface(null, Typeface.BOLD)
        val testRepeatable = TextView(currentContext)
        testRepeatable.background = resources.getDrawable(R.drawable.border)
        testRepeatable.setTypeface(null, Typeface.BOLD)
        firstRow.layoutParams =
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
            )
        textTitle.text = getString(R.string.title_questionnaire)
        testRunning.text = getString(R.string.running)
        testRepeatable.text = getString(R.string.type)

        firstRow.addView(textTitle)
        firstRow.addView(testRunning)
        firstRow.addView(testRepeatable)
        tableQuestionnaires.addView(firstRow, 0)
    }

    /**
     * Calculates the screen diagonal by retrieving the screen pixel size
     *
     * @return a Boolean whether the display has a diagonal of less than 5 inches
     */
    private fun checkIfSmaller5Inches(): Boolean {
        val dm = DisplayMetrics()
        this.activity?.windowManager!!.defaultDisplay.getMetrics(dm)
        val x = Math.pow((dm.widthPixels / dm.xdpi).toDouble(), 2.0)
        val y = Math.pow((dm.heightPixels / dm.ydpi).toDouble(), 2.0)
        val screenInches = sqrt(x + y)
        Log.d("Inch calculation:", screenInches.toString())
        return screenInches < 5
    }

    /**
     * Extract values to be displayed in the table as entries: title, is_active and is_multiple from
     * the JSON response
     *
     * @param current: JSONObject part of an JSONArray that is iterated through
     */
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

    /**
     * Intent start to display the AnswerSheetActivity. Extras that are put are response of the
     * questionnaire structure and questionnaire id.
     *
     * @param response: the retrieved questionnaire raw JSON
     * @param questionnaireID: current questionnaireID with that the questionnaire structure has been
     * fetched
     */
    fun startAnswerSheetActivity(response: JSONObject, questionnaireID: Int) {
        val intent = Intent(currentContext, AnswerSheetActivity::class.java)
        intent.putExtra("response", response.toString())
        intent.putExtra("id", questionnaireID.toString())
        startActivity(intent)
    }

    /**
     * Function for adding a row with a questionnaire
     *
     * @param name extracted actual name of the questionnaire of the raw JSON
     * @param titleName extracted title of the specific JSONObject used in first column
     * @param running boolean value extracted of the specific JSONObject used in second column as icon
     * @param repeat boolean value extracted of the specific JSONObject used in third column as icon
     */
    private fun fillQuestionnaireRow(
        name: String,
        titleName: String,
        running: Boolean,
        repeat: Boolean
    ) {

        val newRow = TableRow(currentContext)
        val title = TextView(currentContext)
        val isSmaller5Inches = checkIfSmaller5Inches()
        if (isSmaller5Inches) {
            title.textSize = 13F
        }
        title.background = resources.getDrawable(R.drawable.border)
        val isRunning = TextView(currentContext)
        isRunning.background = resources.getDrawable(R.drawable.border)
        val isRepeatable = TextView(currentContext)
        isRepeatable.background = resources.getDrawable(R.drawable.border)
        title.text = titleName

        if (running) {
            isRunning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check24dp, 0, 0, 0)
        } else {
            isRunning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross24dp, 0, 0, 0)
        }
        if (repeat) {
            isRepeatable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_repeat24dp, 0, 0, 0)
        } else {
            isRepeatable.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_one_time24dp,
                0,
                0,
                0
            )
        }
        newRow.addView(title)
        newRow.addView(isRunning)
        newRow.addView(isRepeatable)
        newRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        tableQuestionnaires.addView(newRow)
        listenToClickEvents(newRow, name)
    }

    /**
     * Click listener for each row. Used for assigning a questionnaire id to a row.
     *
     * @param row to which TableRow the click listener is attached
     * @param name of the corresponding questionnaire name behind the TableRow
     */
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

    /**
     * Method invoked after retrieving the questionnaire overview. Used to identify the state of
     * filled_out of the registration questionnaire as it has to be filled out first before the others
     * should be displayed.
     *
     * @param response fetched raw json for all questionnaires
     */
    fun allQuestionnairesReceived(response: JSONObject?) {
        var array: JSONArray = response!!.getJSONArray("data")
        var regQuestionnaire: JSONObject? = null
        var regIndex = -1
        for (i in 0 until array.length()) {
            if (array.getJSONObject(i).getJSONObject("attributes").getString("name")
                    .contains("Demography")
            ) {
                regQuestionnaire = array.getJSONObject(i)
                regIndex = i
            }
        }
        if (regQuestionnaire != null && regQuestionnaire.getJSONObject("attributes")
                .getString("is_filled_out") == "false"
        ) {
            array = JSONArray()
            array.put(regQuestionnaire)
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

    /**
     * Toast display to the user if a network error occurs when calling the ProfileFragment.
     */
    fun notifyNetworkError() {
        Toast.makeText(
            currentContext,
            R.string.profile_not_loaded,
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast display to the user if a server error occurs while retrieving profile information.
     */
    fun notifyServerError() {
        Toast.makeText(
            currentContext,
            getString(R.string.server_error_occured),
            Toast.LENGTH_LONG
        ).show()
    }
}