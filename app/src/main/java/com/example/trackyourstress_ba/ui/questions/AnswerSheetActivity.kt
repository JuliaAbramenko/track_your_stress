package com.example.trackyourstress_ba.ui.questions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.R
import org.json.JSONException
import org.json.JSONObject
import android.widget.*
import com.android.volley.VolleyError
import com.example.trackyourstress_ba.QuestionElements.*
import com.example.trackyourstress_ba.kotlin.AnswersheetUtils
import com.example.trackyourstress_ba.ui.home.HomeActivity


open class AnswerSheetActivity : AppCompatActivity() {
    lateinit var response: JSONObject
    lateinit var linearLayout: LinearLayout
    lateinit var title: String
    lateinit var text: String
    lateinit var question: String
    var questionnaireID = 0
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var answersheetUtils: AnswersheetUtils
    var guiList = ArrayList<AnswerElement>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answersheet)
        this.title = getString(R.string.questionnaires)
        linearLayout = findViewById(R.id.answersheetMainView)
    }

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
                //toolbar.title = title TODO manage Toolbar title
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
                    val dateElement = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        DateElement(question, label, this)
                    } else {
                        TODO("VERSION.SDK_INT < N")
                    }
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
        submitButton.text = "Abschicken"
        submitButton.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        this.linearLayout.addView(submitButton)

        submitButton.setOnClickListener {
            answersheetUtils.submitAnswersheet(guiList, questionnaireID, this)
            finishActivity(0) //TODO?
        }
    }

    fun submitSuccess(response: JSONObject) {
        Toast.makeText(this, "Fragebogen abgeschickt!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun submitFail(response: VolleyError) {
        Toast.makeText(this, "Fragebogen konnte nicht abgeschickt werden!", Toast.LENGTH_LONG)
            .show()
    }
}