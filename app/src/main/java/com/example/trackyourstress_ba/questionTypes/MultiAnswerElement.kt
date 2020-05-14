package com.example.trackyourstress_ba.questionTypes

interface MultiAnswerElement : AnswerElement {
    var timestamp: Long
    var label: String
    var selectedValues: ArrayList<String>
}