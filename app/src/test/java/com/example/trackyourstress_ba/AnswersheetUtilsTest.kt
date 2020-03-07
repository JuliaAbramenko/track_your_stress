package com.example.trackyourstress_ba

import com.android.volley.RequestQueue
import com.example.trackyourstress_ba.QuestionElements.AnswerElement
import com.example.trackyourstress_ba.QuestionElements.SingleChoice
import com.example.trackyourstress_ba.kotlin.AnswersheetUtils
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AnswersheetUtilsTest {

    @Mock
    lateinit var requestQueue: RequestQueue

    @Mock
    lateinit var answersheet: AnswerSheetActivity

    @Mock
    lateinit var answerutils: AnswersheetUtils

    @org.junit.Test
    fun submitAnswersheet() {
        answerutils.requestQueue = requestQueue
        var guiList = ArrayList<AnswerElement>(2)
        var map = HashMap<String, String>()
        map["option1"] = "1"
        map["option2"] = "2"
        guiList[0] = SingleChoice("Test", "test01", map, answersheet)

        answerutils.submitAnswersheet(guiList, 1, answersheet)
    }
}