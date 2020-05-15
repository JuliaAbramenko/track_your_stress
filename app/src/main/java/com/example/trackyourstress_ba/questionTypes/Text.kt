package com.example.trackyourstress_ba.questionTypes

import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import android.view.View
import android.graphics.Color.parseColor
import android.os.Build
import android.text.Html
import android.widget.LinearLayout


/**
 * Construction class for a Text GUI element.
 *
 * @property text direct override of the attribute text from AnswerElement as question text
 * @constructor
 *
 * @param caller AnswerSheetActivity as reference to invoke functions from that class
 */
class Text(override var text: String, caller: AnswerSheetActivity) :
    AnswerElement {
    private val textView = TextView(caller)
    private val baseView = caller.linearLayout

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(text)
        } else textView.text = text
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