package com.example.trackyourstress_ba.QuestionElements

import android.graphics.Color
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.view.View
import android.widget.LinearLayout
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity


class SingleChoice(
    questionText: String,
    label: String,
    answers: Map<String, String>,
    caller: AnswerSheetActivity
) : SingleAnswerElement {
    override var text = questionText
    override var label = label
    override var selectedValue = ""
    override var timestamp: Long = 0L
    private val questionTextView = TextView(caller)
    private val baseView = caller.linearLayout
    private val radioGroup = RadioGroup(caller)

    init {
        questionTextView.text = questionText
        baseView.addView(questionTextView)
        for (item in answers) {
            val radioButton = RadioButton(caller)
            radioButton.text = item.value
            radioButton.tag = item.key
            listen(radioButton/*, answers*/)
            radioGroup.addView(radioButton)
        }

        baseView.addView(radioGroup)

        val separator = View(caller)
        separator.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            5
        )
        separator.setBackgroundColor(Color.parseColor("#B3B3B3"))
        baseView.orientation = LinearLayout.VERTICAL
        baseView.addView(separator)
    }

    private fun listen(radioButton: RadioButton/*, answers: Map<String, String>*/) {
        radioButton.setOnClickListener {
            selectedValue = radioButton.tag.toString()
            timestamp = System.currentTimeMillis() / 1000L
        }
    }

}