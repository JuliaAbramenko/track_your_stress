package com.example.trackyourstress_ba.questionTypes

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

/**
 * Construction class for a Slider GUI element. Based on a SeekBar
 *
 * @property text override attribute of AnswerElement as question text. Uses a SeekBar to realize the
 * actual sliding element
 * @property label override attribute of SingleAnswerElement as label. Determined from the beginning
 * @constructor
 *
 * @param sliderValues three values are stored there. "min", "man" and "step" values to determine
 * slider range and step width
 * @param minText previously extracted text that is set at the minimum side of the SeekBar
 * @param maxText previously extracted text that is set at the maximum side of the SeekBar
 * @param caller AnswerSheetActivity as reference to invoke functions from that class
 */
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
        //else it is set to 0 by default
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

    /**
     * Indiviual click listener for every SeekBar. Used to determine selectedValue and timestamp
     * of this element
     *
     * @param seekBar which SeekBar shall be listened to
     */
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