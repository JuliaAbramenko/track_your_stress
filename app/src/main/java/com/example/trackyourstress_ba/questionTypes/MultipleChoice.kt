package com.example.trackyourstress_ba.questionTypes

import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment

class MultipleChoice(questionText: String, answers: Array<String>, caller: QuestionnairesFragment) {
    val questionTextView = TextView(caller.context)
    val text = questionText
    var i = 0

    init {
        questionTextView.text = questionText
    }


}