package com.example.trackyourstress_ba.QuestionElements

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class MultipleChoice(
    questionText: String,
    labelString: String,
    answers: Map<String, String>,
    caller: AnswerSheetActivity
) : MultiAnswerElement {
    override var text = questionText
    override var label = labelString
    override var timestamp = System.currentTimeMillis() / 1000L
    private val questionTextView = TextView(caller)

    var hashMap = HashMap<CheckBox, String>()
    override var selectedValues = ArrayList<String>()
    var timestamps = ArrayList<Long>()
    private val baseView = caller.linearLayout

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            questionTextView.text = Html.fromHtml(text)
        }
        //questionTextView.text = questionText
        baseView.addView(questionTextView)


        for (item in answers) {
            val checkBox = CheckBox(caller)
            checkBox.text = item.value
            //checkBox.text = answers[i]
            hashMap[checkBox] = item.key
            //hashMap.put(checkBox, answers[i])
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

    private fun listen(checkBox: CheckBox) {
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                selectedValues.add(hashMap[checkBox]!!)
                var newTimestamp = System.currentTimeMillis() / 1000L
                timestamps.add(newTimestamp)
                timestamp = timestamps.max()!!

            }
            if (!checkBox.isChecked) {
                selectedValues.remove(hashMap[checkBox])

            }
        }
    }

}