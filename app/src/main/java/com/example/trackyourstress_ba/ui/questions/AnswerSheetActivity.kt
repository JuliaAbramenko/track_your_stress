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
import android.widget.TextView
import org.w3c.dom.Text


class AnswerSheetActivity : AppCompatActivity() {

    lateinit var response: JSONObject
    lateinit var testtext: TextView
    lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answersheet)
        layout = findViewById(R.id.answersheettest)
    }

    override fun onStart() {
        super.onStart()
        try {
            response = JSONObject(intent.getStringExtra("response")!!)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        testtext = TextView(this)
        layout.addView(testtext)
        this.testtext.text = response.toString()


    }
}