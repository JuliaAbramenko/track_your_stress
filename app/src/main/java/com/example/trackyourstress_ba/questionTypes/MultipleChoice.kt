package com.example.trackyourstress_ba.questionTypes

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

/**
 * Construction class for a MultipleChoice GUI element. Based on CheckBoxes
 *
 * @property text override of the AnswerElement attribute as question text
 * @property label override of the SingleAnswerElement attribute. Determined since beginning
 * @constructor
 *
 * @param answers Mapping between keys that correspond to the answer code to the question text
 * @param caller AnswerSheetActivity as reference to invoke functions from that class
 */
class MultipleChoice(
    override var text: String,
    override var label: String,
    answers: Map<String, String>,
    caller: AnswerSheetActivity
) : MultiAnswerElement {
    override var timestamp = 0L
    override var selectedValues = ArrayList<String>()
    private val questionTextView = TextView(caller)
    private val baseView = caller.linearLayout
    private var hashMap = HashMap<CheckBox, String>()
    private var timestamps = ArrayList<Long>()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            questionTextView.text = Html.fromHtml(text)
        } else questionTextView.text = text
        baseView.addView(questionTextView)
        for (item in answers) {
            val checkBox = CheckBox(caller)
            checkBox.text = item.value
            hashMap[checkBox] = item.key
            listen(checkBox)
            baseView.addView(checkBox)
        }
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
     * Individual click listener for each CheckBox. Determines the array of selectedValue as well as
     * the maximum timestamp
     *
     * @param checkBox that needs a click listener
     */
    private fun listen(checkBox: CheckBox) {
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                selectedValues.add(hashMap[checkBox]!!)
                val newTimestamp = System.currentTimeMillis() / 1000L
                timestamps.add(newTimestamp)
                timestamp = timestamps.max()!!
            }
            if (!checkBox.isChecked) {
                selectedValues.remove(hashMap[checkBox])

            }
        }
    }

}