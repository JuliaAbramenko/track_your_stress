package com.example.trackyourstress_ba.questionElements

interface SingleAnswerElement : AnswerElement {
    var timestamp: Long
    var label: String
    var selectedValue: String
}