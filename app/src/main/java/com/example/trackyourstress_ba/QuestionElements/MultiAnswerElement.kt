package com.example.trackyourstress_ba.QuestionElements

interface MultiAnswerElement : AnswerElement {
    var timestamp: Long
    var label: String
    var selectedValues: ArrayList<String>
}