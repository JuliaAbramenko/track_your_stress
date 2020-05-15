package com.example.trackyourstress_ba.ui.questions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.R
import org.json.JSONException
import org.json.JSONObject
import android.widget.*
import com.example.trackyourstress_ba.kotlin.AnswersheetUtils
import com.example.trackyourstress_ba.questionTypes.*
import com.example.trackyourstress_ba.ui.home.HomeActivity

/**
 * Activity that is started when a Questionnaire has been selected for filling out.
 *
 */
open class AnswerSheetActivity : AppCompatActivity() {
    lateinit var response: JSONObject
    lateinit var linearLayout: LinearLayout
    lateinit var title: String
    lateinit var text: String
    private lateinit var question: String
    private var questionnaireID = 0
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var answersheetUtils: AnswersheetUtils
    private var guiList = ArrayList<AnswerElement>()

    /**
     * general creation method of the AnswerSheetActivity
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answersheet)
        this.title = getString(R.string.questionnaires)
        linearLayout = findViewById(R.id.answersheetMainView)
    }

    /**
     * Function invoked when activity is displayed. GUI elements of the questionnaire are created
     * here and added to a list. Button click listeneres are added as well.
     *
     */
    override fun onStart() {
        super.onStart()
        sharedPreferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        try {
            response = JSONObject(intent.getStringExtra("response")!!)
            questionnaireID = intent.getStringExtra("id")!!.toInt()
            answersheetUtils = AnswersheetUtils()

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonArray = response.getJSONArray("data")
        for (i in 0 until jsonArray.length()) {
            val item: JSONObject = jsonArray.getJSONObject(i)
            val current = item.getJSONObject("attributes").getJSONObject("content")
            if (current.has("headline") && current.getString("headline").isNotEmpty()) {
                title = current.getString("headline")
                Headline(title, this)
            }
            if (current.has("text") && current.getString("text").isNotEmpty()) {
                text = current.getString("text")
                val textElement =
                    Text(text, this)
                guiList.add(textElement)
            }
            // check which question type and create GUI objects specifically
            if (current.has("question") && current.getString("question").isNotEmpty()) {
                question = current.getString("question")
                val label = current.getString("label")

                if (current.getString("questiontype") == "TextDate") {
                    val label = current.getString("label")
                    val dateElement =
                        DateElement(
                            question,
                            label,
                            this
                        )

                    guiList.add(dateElement)
                }
                if (current.getString("questiontype") == "Slider") {
                    val sliderMin = current.getJSONObject("values").getString("min").toInt()
                    val sliderMax = current.getJSONObject("values").getString("max").toInt()
                    val sliderStep = current.getJSONObject("values").getString("step").toInt()
                    val sliderList = arrayOf(sliderMin, sliderMax, sliderStep)
                    val minText =
                        current.getJSONArray("answers").getJSONObject(0).getString("label")
                    val maxText =
                        current.getJSONArray("answers").getJSONObject(1).getString("label")
                    val sliderElement =
                        Slider(
                            question,
                            label,
                            sliderList,
                            minText,
                            maxText,
                            this
                        )
                    guiList.add(sliderElement)
                }
                if (current.getString("questiontype") == "MultipleChoice") {
                    val mcValues = current.getJSONArray("answers")
                    val mcList = Array(mcValues.length()) {
                        mcValues.getString(it)
                    }
                    val values = current.getJSONArray("values")
                    val valueList = Array(values.length()) {
                        values.getString(it)
                    }
                    val zipped = valueList.zip(mcList).toMap()
                    val mcElement =
                        MultipleChoice(
                            question,
                            label,
                            zipped,
                            this
                        )
                    guiList.add(mcElement)
                }
                if (current.getString("questiontype") == "SingleChoice") {
                    val scValues = current.getJSONArray("answers")
                    val scList = Array(scValues.length()) {
                        scValues.getString(it)
                    }
                    val values = current.getJSONArray("values")
                    val valueList = Array(values.length()) {
                        values.getString(it)
                    }
                    val zipped = valueList.zip(scList).toMap()
                    val scElement =
                        SingleChoice(
                            question,
                            label,
                            zipped,
                            this
                        )
                    guiList.add(scElement)
                }
                if (current.getString("questiontype") == "SAMScaleFace") {
                    val samFaceElement =
                        SAMScaleFace(
                            question,
                            label,
                            this
                        )
                    guiList.add(samFaceElement)
                }
                if (current.getString("questiontype") == "SAMScaleBody") {
                    val samBodyElement =
                        SAMScaleBody(
                            question,
                            label,
                            this
                        )
                    guiList.add(samBodyElement)
                }
            }
        }
        val submitButton = Button(this)
        submitButton.text = getString(R.string.submit)
        submitButton.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        this.linearLayout.addView(submitButton)

        submitButton.setOnClickListener {
            val reduced = reduceToAnswerElements(guiList)
            val allQuestionsHaveAnswers = checkIfAllQuestionsHaveAnswers(reduced)
            if (allQuestionsHaveAnswers) {
                answersheetUtils.submitAnswersheet(guiList, questionnaireID, this)
                finishActivity(0)
            } else showIncomplete()
        }
    }

    /**
     * Checks whether all questions that require an answer have already a selected answer.
     *
     * @param reducedGuiList List in which only SingleAnwerElements or MultipleAnswerElements exist
     * @return whether all questions have an answer registered in a Boolean
     */
    private fun checkIfAllQuestionsHaveAnswers(reducedGuiList: ArrayList<AnswerElement>): Boolean {
        var result = false
        for (item in reducedGuiList) {
            if (item is SingleAnswerElement && item.selectedValue == "") {
                result = false
                break
            } else if (item is MultiAnswerElement && item.timestamp == 0L) {
                result = false
                break
            } else result = true
        }
        return result
    }

    /**
     * Reducing operation to extract questions that require an answer not text elements.
     *
     * @param guiList The list of which SingleAnswerElements or MultipleAnswerElements shall be
     * extracted
     * @return an ArrayList<AnswerElement> that do not contain text elements anymore
     */
    private fun reduceToAnswerElements(guiList: ArrayList<AnswerElement>): ArrayList<AnswerElement> {
        val elementsWithNecessaryAnswers = ArrayList<AnswerElement>()
        for (item in guiList) {
            if (item is SingleAnswerElement || item is MultiAnswerElement) {
                elementsWithNecessaryAnswers.add(item)
            }
        }
        return elementsWithNecessaryAnswers
    }

    /**
     * Played Toast when an answersheet has been submitted successfully.
     *
     */
    fun submitSuccess() {
        Toast.makeText(this, getString(R.string.quesitionnaire_submitted), Toast.LENGTH_LONG).show()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    /**
     * Played Toast when an answersheet has not been submitted successfully.
     *
     */
    fun submitFail() {
        Toast.makeText(
            this,
            getString(R.string.answersheet_could_not_be_submitted),
            Toast.LENGTH_LONG
        )
            .show()
    }

    /**
     * Displayed Toast when checkIfAllQuestionsHaveAnswers is false
     *
     */
    private fun showIncomplete() {
        Toast.makeText(
            this,
            getString(R.string.answers_missing),
            Toast.LENGTH_LONG
        )
            .show()
    }
}