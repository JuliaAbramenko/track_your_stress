package com.example.trackyourstress_ba.QuestionElements

import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class Headline(headlineText: String, caller: AnswerSheetActivity) :
    AnswerElement {
    override var text = headlineText
    private val headlineView = TextView(caller)
    private val baseView = caller.linearLayout

    init {
        headlineView.text = text
        headlineView.textSize = 24F
        baseView.addView(headlineView)
    }
}