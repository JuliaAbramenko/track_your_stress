package com.example.trackyourstress_ba.questionTypes

interface SingleAnswerElement : AnswerElement {
    var timestamp: Long
    var label: String
    var selectedValue: String
}