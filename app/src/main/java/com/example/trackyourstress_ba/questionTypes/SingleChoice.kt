package com.example.trackyourstress_ba.questionTypes

import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment
import android.view.Gravity
import android.widget.LinearLayout
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity


class SingleChoice(questionText: String, answers: Array<String>, caller: AnswerSheetActivity) {

    private val questionTextView = TextView(caller)
    private val baseView = caller.linearLayout
    private var i = 0
    private val radioGroup = RadioGroup(caller)

    init {
        questionTextView.text = questionText
        baseView.addView(questionTextView)
        for (item in answers) {
            val radioButton = RadioButton(caller)
            radioButton.text = answers[i]
            radioGroup.addView(radioButton)
            i++
        }

        /*val params = LinearLayout.LayoutParams(
            RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT
        )
        params.weight = 1.0f
        params.gravity = Gravity.CENTER

        radioGroup.layoutParams = params */
        baseView.addView(radioGroup)
    }
}