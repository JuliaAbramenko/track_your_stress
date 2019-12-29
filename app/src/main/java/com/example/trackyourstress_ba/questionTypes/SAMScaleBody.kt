package com.example.trackyourstress_ba.questionTypes

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
        for (i: Int in images.indices) {
            val imageView = ImageView(caller)
            val image = images[i]
            imageView.setImageResource(image)
            val radioButton = RadioButton(caller)
            radioGroup.addView(radioButton)
            baseView.addView(imageView)
        }
        baseView.addView(radioGroup)

    }
}