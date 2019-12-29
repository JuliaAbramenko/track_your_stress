package com.example.trackyourstress_ba.questionTypes

import android.widget.CheckBox
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class MultipleChoice(questionText: String, answers: Array<String>, caller: AnswerSheetActivity) {

    private val questionTextView = TextView(caller)
    val text = questionText
    var i = 0
    var hashMap = HashMap<CheckBox, String>()
    var selectedValues = ArrayList<String>()
    private val baseView = caller.linearLayout

    init {
        questionTextView.text = questionText
        baseView.addView(questionTextView)
        for (item in answers) {
            val checkBox = CheckBox(caller)
            checkBox.text = answers[i]
            hashMap.put(checkBox, answers[i])
            i++
            listen(checkBox)
            baseView.addView(checkBox)
        }
    }

    //TODO get values of answers
    fun listen(checkBox: CheckBox) {
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                selectedValues.add(hashMap[checkBox]!!)
            }
        }
    }

}