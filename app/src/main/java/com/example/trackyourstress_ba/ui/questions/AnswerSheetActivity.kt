package com.example.trackyourstress_ba.ui.questions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.R
import org.json.JSONException
import org.json.JSONObject
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import com.example.trackyourstress_ba.questionTypes.*
import org.w3c.dom.Text


class AnswerSheetActivity : AppCompatActivity() {

    lateinit var response: JSONObject
    //lateinit var testtext: TextView
    lateinit var linearLayout: LinearLayout
    lateinit var title: String
    lateinit var text: String
    lateinit var question: String

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
            }
            // check whether there is text instead
            if (current.has("text") && current.getString("text").isNotEmpty()) {
                text = current.getString("text")
            }

            // check which question type and create GUI objects specifically
            if (current.has("question") && current.getString("question").isNotEmpty()) {
                question = current.getString("question")
                if (current.getString("questiontype") == "Slider") {
                    val sliderMin = current.getJSONObject("values").getString("min").toInt()
                    val sliderMax = current.getJSONObject("values").getString("max").toInt()
                    val sliderStep = current.getJSONObject("values").getString("step").toInt()
                    val sliderList = arrayOf(sliderMin, sliderMax, sliderStep)

                    /*val sliderValues = current.getJSONArray("values")
                    val sliderList = Array(sliderValues.length()) {
                        sliderValues.getInt(it)
                    }*/
                    val minText =
                        current.getJSONArray("answers").getJSONObject(0).getString("label")
                    val maxText =
                        current.getJSONArray("answers").getJSONObject(1).getString("label")
                    Slider(question, sliderList, minText, maxText, this)
                }
                if (current.getString("questiontype") == "MultipleChoice") {
                    val mcValues = current.getJSONArray("answers")
                    val mcList = Array(mcValues.length()) {
                        mcValues.getString(it)
                    }
                    MultipleChoice(question, mcList, this)
                }
                if (current.getString("questiontype") == "SingleChoice") {
                    val scValues = current.getJSONArray("answers")
                    val scList = Array(scValues.length()) {
                        scValues.getString(it)
                    }
                    SingleChoice(question, scList, this)
                }
                if (current.getString("questiontype") == "SAMScaleFace") {
                    /*val samScaleFaceValues = current.getJSONArray("values")
                    val samScaleFaceList = Array(samScaleFaceValues.length()) {
                        samScaleFaceValues.getInt(it)
                    }*/
                    SAMScaleFace(question, this)
                }
                if (current.getString("questiontype") == "SAMScaleBody") {
                    /*val samScaleBodyValues = current.getJSONArray("values")
                    val samScaleBodyList = Array(samScaleBodyValues.length()) {
                        samScaleBodyValues.getInt(it)
                    }*/
                    SAMScaleBody(question, this)
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


    }
}