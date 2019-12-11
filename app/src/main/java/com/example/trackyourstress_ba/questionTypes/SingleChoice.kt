package com.example.trackyourstress_ba.questionTypes

import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment
import android.view.Gravity
import android.widget.LinearLayout


class SingleChoice(questionText: String, answers: Array<String>, caller: QuestionnairesFragment) {
    val questionTextView = TextView(caller.context)
    val text = questionText
    var i = 0
    private val radioGroup = RadioGroup(caller.context)

    init {
        questionTextView.text = questionText
        for (item in answers) {
            val radioButton = RadioButton(caller.context)
            radioButton.text = answers[i]
            radioGroup.addView(radioButton)
            i++
        }

        val params = LinearLayout.LayoutParams(
            RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT
        )
        params.weight = 1.0f
        params.gravity = Gravity.CENTER

        radioGroup.layoutParams = params
    }
}