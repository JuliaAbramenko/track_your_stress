package com.example.trackyourstress_ba.questionTypes

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.Gravity
import android.view.View
import android.widget.*
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

/**
 * Construction class for a SAMScaleBody GUI element. Based on a RadioGroup
 *
 * @property text override of the AnswerElement attribute as question text
 * @property label override of the SingleAnswerElement attribute. Determined since beginning
 * @constructor
 *
 * @param caller AnswerSheetActivity as reference to invoke functions from that class
 */
class SAMScaleBody(
    override var text: String,
    override var label: String,
    caller: AnswerSheetActivity
) :
    SingleAnswerElement {
    private var images = arrayOf(
        R.drawable.sambody1, R.drawable.empty, R.drawable.sambody2,
        R.drawable.empty, R.drawable.sambody3, R.drawable.empty, R.drawable.sambody4,
        R.drawable.empty, R.drawable.sambody5
    )
    private val radioGroup = RadioGroup(caller)
    private val questionTextView = TextView(caller)
    private val baseView = caller.linearLayout
    override var selectedValue = ""
    override var timestamp = 0L
    private var i = 1

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            questionTextView.text = Html.fromHtml(text)
        } else questionTextView.text = text
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

    /**
     * Individual click listener for each RadioButton to identify timestamp and selectedValue
     *
     * @param radioButton RadioButton that needs a click listener
     */
    private fun listen(radioButton: RadioButton) {
        radioButton.setOnClickListener {
            timestamp = System.currentTimeMillis() / 1000L
            selectedValue = radioButton.tag.toString()
        }
    }

}