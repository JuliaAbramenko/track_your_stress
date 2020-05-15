package com.example.trackyourstress_ba.questionTypes

import android.os.Build
import android.text.Html
import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

/**
 *
 * Construction class for a Headline GUI element.
 * @property text direct override of the attribute text from AnswerElement as question text
 * @constructor
 *
 * @param caller AnswerSheetActivity as reference to invoke functions from that class
 */
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