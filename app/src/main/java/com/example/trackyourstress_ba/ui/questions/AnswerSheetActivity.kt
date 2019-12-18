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
import android.widget.ScrollView
import android.widget.TextView
import org.w3c.dom.Text


class AnswerSheetActivity : AppCompatActivity() {

    lateinit var response: JSONObject
    //lateinit var testtext: TextView
    lateinit var linearLayout: LinearLayout
    lateinit var title: String

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
        //testtext = TextView(this)
        //layout.addView(testtext)
        //this.testtext.text = response.toString()
        val jsonArray = response.getJSONArray("data")
        for (i in 0 until jsonArray.length()) {
            val item: JSONObject = jsonArray.getJSONObject(i)
            if (i == 0 || i == 1) {
                title =
                    item.getJSONObject("attributes").getJSONObject("content").getString("headline")
            } else {
                val questionText: String =
                    item.getJSONObject("attributes").getJSONObject("content").getString("question")
                val questionTextView = TextView(this)
                questionTextView.text = questionText
                linearLayout.addView(questionTextView)
            }
        }


    }
}