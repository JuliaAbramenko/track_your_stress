package com.example.trackyourstress_ba.questionTypes

/**
 * Implementation of the AnswerElement base. Used for all question types that require precisely one
 * answer
 *
 */
interface SingleAnswerElement : AnswerElement {
    var timestamp: Long
    var label: String
    var selectedValue: String
}