package com.example.trackyourstress_ba.questionTypes

import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class MultipleChoice(
    questionText: String,
    answers: Map<String, String>,
    caller: AnswerSheetActivity
) : QuestionType {
    override val questionText = questionText
    private val questionTextView = TextView(caller)

    var hashMap = HashMap<CheckBox, String>()
    var selectedValues = ArrayList<String>()
    var timestamps = ArrayList<Long>()
    private val baseView = caller.linearLayout

    init {
        questionTextView.text = questionText
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
                timestamps.add(System.currentTimeMillis() / 1000L)

            }
            if (!checkBox.isChecked) {
                selectedValues.remove(hashMap[checkBox])

            }
        }
    }

    private fun getValues(): ArrayList<String> {
        return selectedValues
    }

    private fun getTimestamp(): Long {
        return timestamps.max()!!
    }

}