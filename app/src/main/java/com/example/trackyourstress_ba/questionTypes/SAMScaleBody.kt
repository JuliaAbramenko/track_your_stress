package com.example.trackyourstress_ba.questionTypes

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class SAMScaleBody(textOfQuestion: String, caller: AnswerSheetActivity) {
    var images = arrayOf(
        R.drawable.sambody1, R.drawable.empty, R.drawable.sambody2,
        R.drawable.empty, R.drawable.sambody3, R.drawable.empty, R.drawable.sambody4,
        R.drawable.empty, R.drawable.sambody5
    )
    private val radioGroup = RadioGroup(caller)
    private val questionTextView = TextView(caller)
    private val questionText = textOfQuestion
    private val baseView = caller.linearLayout
    //private val adapter = ArrayAdapter<>(caller, R.layout.activity_answersheet, )


    init {
        questionTextView.text = questionText
        baseView.addView(questionTextView)
        for (item in images) {
            /*val imageView = ImageView(caller)
            val image = images[i]*/
            val radioButton = RadioButton(caller)
            //radioButton.gravity = Gravity.CENTER
            //radioButton.setPadding(0, 50, 0 , 0)
            radioButton.gravity = Gravity.START
            radioGroup.addView(radioButton)
            //radioButton.buttonDrawable = caller.resources.getDrawable(images[i])
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
}