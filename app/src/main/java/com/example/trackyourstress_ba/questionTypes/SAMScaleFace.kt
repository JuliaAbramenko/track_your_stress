package com.example.trackyourstress_ba.questionTypes

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.*
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity


class SAMScaleFace(textOfQuestion: String, caller: AnswerSheetActivity) : QuestionType {
    private var images = arrayOf(
        R.drawable.samface1, R.drawable.empty, R.drawable.samface2,
        R.drawable.empty, R.drawable.samface3, R.drawable.empty, R.drawable.samface4,
        R.drawable.empty, R.drawable.samface5
    )
    private val radioGroup = RadioGroup(caller)
    private val questionTextView = TextView(caller)
    private var selectedValue = ""
    private var timestamp = 0L
    private var i = 1
    override val questionText = textOfQuestion
    private val baseView = caller.linearLayout


    init {
        questionTextView.text = questionText
        baseView.addView(questionTextView)
        for (item in images) {
            val radioButton = RadioButton(caller)
            radioButton.gravity = Gravity.START
            radioButton.tag = i.toString()
            i++
            radioGroup.addView(radioButton)
            listen(radioButton)
            val imageView = ImageView(caller)
            imageView.setImageResource(item)
            radioGroup.addView(imageView)
            val params = LinearLayout.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT
            )
            params.weight = 1.0f
            params.gravity = Gravity.RELATIVE_LAYOUT_DIRECTION
            radioGroup.layoutParams = params
        }

        val separator = View(caller)
        separator.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            5
        )
        separator.setBackgroundColor(Color.parseColor("#B3B3B3"))
        baseView.orientation = LinearLayout.VERTICAL
        baseView.addView(radioGroup)
        baseView.addView(separator)


    }

    private fun listen(radioButton: RadioButton) {
        radioButton.setOnClickListener {
            if (radioButton.isSelected) {
                selectedValue = radioButton.tag.toString()
                timestamp = System.currentTimeMillis() / 1000L
            }

        }
    }

    private fun getValue(): String {
        return selectedValue
    }

    private fun getTimestamp(): Long {
        return timestamp
    }
}
