package com.example.trackyourstress_ba.questionTypes

import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class MultipleChoice//(questionText: String, answers: Array<String>, caller: AnswerSheetActivity) {
    (questionText: String, answers: Map<String, String>, caller: AnswerSheetActivity) {
    private val questionTextView = TextView(caller)
    val text = questionText
    var hashMap = HashMap<CheckBox, String>()
    var selectedValues = ArrayList<String>()
    private val baseView = caller.linearLayout

    init {
        questionTextView.text = questionText
        baseView.addView(questionTextView)
        for (item in answers) {
            val checkBox = CheckBox(caller)
            checkBox.text = item.value
            //checkBox.text = answers[i]
            hashMap.put(checkBox, item.key)
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

    //TODO get values of answers
    fun listen(checkBox: CheckBox) {
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                selectedValues.add(hashMap[checkBox]!!)
                checkBox.text = "value = " + hashMap[checkBox]
            }
            if (!checkBox.isChecked) {
                selectedValues.remove(hashMap[checkBox])
                checkBox.text = hashMap[checkBox]
            }
        }
    }

}