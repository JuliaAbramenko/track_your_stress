package com.example.trackyourstress_ba.questionElements

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class Slider(
    override var text: String,
    override var label: String,
    sliderValues: Array<Int>,
    minText: String,
    maxText: String,
    caller: AnswerSheetActivity
) : SingleAnswerElement {

    private val baseView = caller.linearLayout
    private val questionTextView = TextView(caller)
    private val seekBar = SeekBar(caller)
    private val min = sliderValues[0]
    private val max = sliderValues[1]
    private val step = sliderValues[2]
    override var selectedValue = ""
    override var timestamp = 0L
    private val minTextView = TextView(caller)
    private val maxTextView = TextView(caller)

    init {
        val grid = GridLayout(caller)
        grid.columnCount = 2
        grid.rowCount = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            questionTextView.text = Html.fromHtml(text)
        } else questionTextView.text = text

        questionTextView.gravity = Gravity.CENTER
        seekBar.max = (max - min) / step
        seekBar.progress = (max - min) / 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.min = min
        }
        //TODO??
        maxTextView.gravity = Gravity.END
        minTextView.gravity = Gravity.START
        minTextView.text = minText
        maxTextView.text = maxText
        listen(seekBar)

        baseView.addView(questionTextView)
        baseView.addView(seekBar)
        baseView.addView(grid)
        grid.addView(
            minTextView,
            GridLayout.LayoutParams(
                GridLayout.spec(0, GridLayout.START),
                GridLayout.spec(0, GridLayout.END)
            )
        )
        grid.addView(
            maxTextView,
            GridLayout.LayoutParams(
                GridLayout.spec(0, GridLayout.START),
                GridLayout.spec(1, GridLayout.END)
            )
        )
        val separator = View(caller)
        separator.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            5
        )
        separator.setBackgroundColor(Color.LTGRAY)
        baseView.addView(separator)
    }

    private fun listen(seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                selectedValue = i.toString()
                timestamp = System.currentTimeMillis() / 1000L
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }


}