package com.example.trackyourstress_ba.questionTypes

import android.widget.CheckBox
import android.widget.TextView
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity

class MultipleChoice(questionText: String, answers: Array<String>, caller: AnswerSheetActivity) {

    val context = caller
    val questionTextView = TextView(context)
    val text = questionText
    var i = 0
    var hashMap = HashMap<CheckBox, String>()
    var selectedValues = ArrayList<String>()

    init {
        questionTextView.text = questionText
        for (i in answers.indices) {
            val checkBox = CheckBox(context)
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