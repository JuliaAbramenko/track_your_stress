package com.example.trackyourstress_ba.questionTypes

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class SAMScaleFace(textOfQuestion: String, caller: AnswerSheetActivity) {
    var images = arrayOf(
        R.drawable.samface1, R.drawable.empty, R.drawable.samface2,
        R.drawable.empty, R.drawable.samface3, R.drawable.empty, R.drawable.samface4,
        R.drawable.empty, R.drawable.samface5
    )
    private val radioGroup = RadioGroup(caller)
    private val questionTextView = TextView(caller)
    private val questionText = textOfQuestion
    private val baseView = caller.linearLayout
    //private val adapter = ArrayAdapter<>(caller, R.layout.activity_answersheet, )


    init {
        questionTextView.text = questionText
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
