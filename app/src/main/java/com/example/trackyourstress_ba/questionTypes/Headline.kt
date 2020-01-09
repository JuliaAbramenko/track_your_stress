package com.example.trackyourstress_ba.questionTypes

import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class Headline(headlineText: String, caller: AnswerSheetActivity) : QuestionType {
    override val questionText = headlineText
    private val headlineView = TextView(caller)
    private val baseView = caller.linearLayout

    init {
        headlineView.text = questionText
        headlineView.textSize = 24F
        baseView.addView(headlineView)
    }
}