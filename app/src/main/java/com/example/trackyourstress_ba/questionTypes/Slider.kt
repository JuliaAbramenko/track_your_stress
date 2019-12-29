package com.example.trackyourstress_ba.questionTypes

import android.os.Build
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class Slider(
    textOfQuestion: String,
    sliderValues: Array<Int>,
    minText: String,
    maxText: String,
    caller: AnswerSheetActivity
) {

    private val baseView = caller.linearLayout
    private val questionTextView = TextView(caller)
    private val questionText = textOfQuestion
    private val seekBar = SeekBar(caller)
    private val min = sliderValues[0]
    private val max = sliderValues[1]
    private val step = sliderValues[2]
    private var selectedValue = 0
    private val minTextView = TextView(caller)
    private val maxTextView = TextView(caller)

    init {
        questionTextView.text = questionText
        questionTextView.gravity = Gravity.CENTER
        minTextView.gravity = Gravity.START
        maxTextView.gravity = Gravity.END
        minTextView.text = minText
        maxTextView.text = maxText
        seekBar.max = (max - min) / step
        seekBar.progress = (max - min) / 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.min = min
        }

        baseView.addView(questionTextView)
        baseView.addView(seekBar)
        baseView.addView(minTextView)
        baseView.addView(maxTextView)
        listen(seekBar)
        //baseView.
    }

    private fun listen(seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                selectedValue = i
                //preliminary and TODO
                minTextView.text = "$i"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

}