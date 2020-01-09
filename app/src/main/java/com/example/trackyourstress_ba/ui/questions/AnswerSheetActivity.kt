package com.example.trackyourstress_ba.ui.questions

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.R
import org.json.JSONException
import org.json.JSONObject
import android.widget.*
import com.example.trackyourstress_ba.questionTypes.*


class AnswerSheetActivity : AppCompatActivity() {

    lateinit var response: JSONObject
    lateinit var linearLayout: LinearLayout
    lateinit var title: String
    lateinit var text: String
    lateinit var question: String
    var guiList = ArrayList<QuestionType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answersheet)
        linearLayout = findViewById(R.id.answersheetMainView)
    }

    override fun onStart() {
        super.onStart()
        try {
            response = JSONObject(intent.getStringExtra("response")!!)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonArray = response.getJSONArray("data")
        for (i in 0 until jsonArray.length()) {
            val item: JSONObject = jsonArray.getJSONObject(i)
            val current = item.getJSONObject("attributes").getJSONObject("content")
            //check if there is a headline to show
            if (current.has("headline") && current.getString("headline").isNotEmpty()) {
                title = current.getString("headline")
                Headline(title, this)
                //val toolbar = findViewById<Toolbar>(R.id.toolbar)
                //toolbar.title = title TODO manage Toolbar title
            }
            // check whether there is text instead
            if (current.has("text") && current.getString("text").isNotEmpty()) {
                text = current.getString("text")
                val textElement = Text(text, this)
                guiList.add(textElement)
            }

            // check which question type and create GUI objects specifically
            if (current.has("question") && current.getString("question").isNotEmpty()) {
                question = current.getString("question")
                if (current.getString("questiontype") == "Slider") {
                    val sliderMin = current.getJSONObject("values").getString("min").toInt()
                    val sliderMax = current.getJSONObject("values").getString("max").toInt()
                    val sliderStep = current.getJSONObject("values").getString("step").toInt()
                    val sliderList = arrayOf(sliderMin, sliderMax, sliderStep)
                    val minText =
                        current.getJSONArray("answers").getJSONObject(0).getString("label")
                    val maxText =
                        current.getJSONArray("answers").getJSONObject(1).getString("label")
                    val sliderElement = Slider(question, sliderList, minText, maxText, this)
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
                    val mcElement = MultipleChoice(question, zipped, this)
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
                    val scElement = SingleChoice(question, zipped, this)
                    guiList.add(scElement)
                }
                if (current.getString("questiontype") == "SAMScaleFace") {
                    /*val samScaleFaceValues = current.getJSONArray("values")
                    val samScaleFaceList = Array(samScaleFaceValues.length()) {
                        samScaleFaceValues.getInt(it)
                    }*/
                    val samFaceElement = SAMScaleFace(question, this)
                    guiList.add(samFaceElement)
                }
                if (current.getString("questiontype") == "SAMScaleBody") {
                    /*val samScaleBodyValues = current.getJSONArray("values")
                    val samScaleBodyList = Array(samScaleBodyValues.length()) {
                        samScaleBodyValues.getInt(it)
                    }*/
                    val samBodyElement = SAMScaleBody(question, this)
                    guiList.add(samBodyElement)
                }

            }

            /*else {
                val questionText: String =
                    item.getJSONObject("attributes").getJSONObject("content").getString("question")
                val questionTextView = TextView(this)
                questionTextView.text = questionText
                listView.addView(questionTextView)
            }*/
        }
        val submitButton = Button(this)
        submitButton.text = "Abschicken"
        submitButton.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        this.linearLayout.addView(submitButton)

        submitButton.setOnClickListener {
            for (question in guiList) {

            }
        }

    }
}