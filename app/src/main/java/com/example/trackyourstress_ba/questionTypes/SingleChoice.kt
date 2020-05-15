package com.example.trackyourstress_ba.questionTypes

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.view.View
import android.widget.LinearLayout
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

/**
 * Construction class for a SingleChoice GUI element. Based on a RadioGroup
 *
 * @property text override of the interface attribute text as question text
 * @property label override of the interface SingleAnswerElement attribute label (is passed from
 * the caller and determined from the beginning)
 * @constructor
 *
 * @param answers Map used for mapping between hidden tags of RadioButtons and the corresponding
 * test displayed on the RadioButton
 * @param caller AnswerSheetActivity as reference to invoke functions from that class
 */
class SingleChoice(
    override var text: String,
    override var label: String,
    answers: Map<String, String>,
    caller: AnswerSheetActivity
) : SingleAnswerElement {
    override var selectedValue = ""
    override var timestamp: Long = 0L
    private val questionTextView = TextView(caller)
    private val baseView = caller.linearLayout
    private val radioGroup = RadioGroup(caller)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            questionTextView.text = Html.fromHtml(text)
        } else questionTextView.text = text

        baseView.addView(questionTextView)
        for (item in answers) {
            val radioButton = RadioButton(caller)
            radioButton.text = item.value
            radioButton.tag = item.key
            listen(radioButton/*, answers*/)
            radioGroup.addView(radioButton)
        }
        baseView.addView(radioGroup)

        val separator = View(caller)
        separator.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            5
        )
        separator.setBackgroundColor(Color.parseColor("#B3B3B3"))
        baseView.orientation = LinearLayout.VERTICAL
        baseView.addView(separator)
    }

    /**
     * Individual click listener implementation for each radio button to determine relevant values selectedValue and
     * timestamp.
     *
     * @param radioButton which RadioButton shall be listened to
     */
    private fun listen(radioButton: RadioButton) {
        radioButton.setOnClickListener {
            selectedValue = radioButton.tag.toString()
            timestamp = System.currentTimeMillis() / 1000L
        }
    }
}