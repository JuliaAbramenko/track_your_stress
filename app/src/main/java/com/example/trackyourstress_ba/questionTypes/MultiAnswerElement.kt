package com.example.trackyourstress_ba.questionTypes

/**
 * Implementation of the AnswerElement. Used for questions that allow zero, one or more answers
 *
 */
interface MultiAnswerElement : AnswerElement {
    var timestamp: Long
    var label: String
    var selectedValues: ArrayList<String>
}