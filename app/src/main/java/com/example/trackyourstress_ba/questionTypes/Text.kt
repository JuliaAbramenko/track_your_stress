package com.example.trackyourstress_ba.questionTypes

import android.graphics.Paint
import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.view.ContextThemeWrapper
import android.graphics.Color.parseColor
import android.widget.LinearLayout
import android.R
import android.graphics.Color
import android.view.ViewGroup


class Text(text: String, caller: AnswerSheetActivity) {

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