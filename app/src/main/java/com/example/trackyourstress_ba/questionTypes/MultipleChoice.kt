package com.example.trackyourstress_ba.questionTypes

import android.widget.CheckBox
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment

class MultipleChoice(questionText: String, answers: Array<String>, caller: QuestionnairesFragment) {
    val questionTextView = TextView(caller.context)
    val text = questionText
    var i = 0
    var hashMap = HashMap<CheckBox, String>()
    var selectedValues = ArrayList<String>()

    init {
        questionTextView.text = questionText
        for (i in answers.indices) {
            val checkBox = CheckBox(caller.context)
            checkBox.text = answers[i]
            hashMap.put(checkBox, answers[i])
            listen(checkBox)
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