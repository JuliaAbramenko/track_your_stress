package com.example.trackyourstress_ba.questionTypes

import android.widget.SeekBar
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment

class Slider(textOfQuestion: String, sliderValues: Array<Int>, caller: QuestionnairesFragment) {

    val questionTextView = TextView(caller.context)
    val questionText = textOfQuestion
    val seekBar = SeekBar(caller.context)
    val min = sliderValues[0]
    val max = sliderValues[1]
    val step = sliderValues[2]
    val stepTextView = TextView(caller.context)

    init {
        questionTextView.text = questionText
        seekBar.max = (max - min) / step
        stepTextView.text = "0"
        listen(seekBar)
    }

    fun listen(seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                stepTextView.text = "Progress : $i"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })
    }

}