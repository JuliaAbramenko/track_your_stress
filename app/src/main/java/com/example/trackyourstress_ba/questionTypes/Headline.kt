package com.example.trackyourstress_ba.questionTypes

import android.os.Build
import android.text.Html
import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class Headline(override var text: String, caller: AnswerSheetActivity) :
    AnswerElement {
    private val headlineView = TextView(caller)
    private val baseView = caller.linearLayout

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            headlineView.text = Html.fromHtml(text)
        } else headlineView.text = text
        headlineView.textSize = 24F
        baseView.addView(headlineView)
    }
}