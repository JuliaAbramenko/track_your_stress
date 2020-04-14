package com.example.trackyourstress_ba.questionElements

interface MultiAnswerElement : AnswerElement {
    var timestamp: Long
    var label: String
    var selectedValues: ArrayList<String>
}