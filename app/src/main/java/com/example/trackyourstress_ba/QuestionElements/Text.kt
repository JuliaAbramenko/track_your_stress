package com.example.trackyourstress_ba.QuestionElements

import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import android.view.View
import android.graphics.Color.parseColor
import android.widget.LinearLayout


class Text(text: String, caller: AnswerSheetActivity) :
    AnswerElement {

    override var text = text

    private val textView = TextView(caller)
    private val baseView = caller.linearLayout

    init {
        textView.text = text
        val separator = View(caller)
        separator.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            5
        )
        separator.setBackgroundColor(parseColor("#B3B3B3"))
        baseView.orientation = LinearLayout.VERTICAL
        baseView.addView(textView)
        baseView.addView(separator)
    }
}