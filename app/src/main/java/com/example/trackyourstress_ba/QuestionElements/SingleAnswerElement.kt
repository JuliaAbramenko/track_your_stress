package com.example.trackyourstress_ba.QuestionElements

interface SingleAnswerElement : AnswerElement {
    var timestamp: Long
    var label: String
    var selectedValue: String

}