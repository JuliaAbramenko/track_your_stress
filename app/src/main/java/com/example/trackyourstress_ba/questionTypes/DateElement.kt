package com.example.trackyourstress_ba.questionTypes

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.questions.AnswerSheetActivity
import java.util.*
import java.text.SimpleDateFormat

/**
 * Construction class for a DateElement GUI object. Based of an EditText with a click listener coped
 * with a native DatePickerDialog
 *
 * @property text direct override of the AnswerElement attribute as question text
 * @property label override of the SingleAnswerElement attribute. Determined from the beginning
 * @constructor
 *
 * @param caller AnswerSheetActivity as reference to invoke functions from that class
 */
class DateElement(
    override var text: String,
    override var label: String,
    caller: AnswerSheetActivity
) : SingleAnswerElement {
    override var selectedValue = ""
    override var timestamp: Long = 0L
    private lateinit var datePickerDialog: DatePickerDialog
    private val textView = TextView(caller)
    private val editText = EditText(caller)
    private val baseView = caller.linearLayout

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(text)
        } else textView.text = text
        baseView.orientation = LinearLayout.VERTICAL
        baseView.addView(textView)
        editText.inputType = InputType.TYPE_NULL
        baseView.addView(editText)
        listen(editText, caller)
        val separator = View(caller)
        separator.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            5
        )
        separator.setBackgroundColor(Color.parseColor("#B3B3B3"))
        baseView.addView(separator)
    }

    /**
     * Click listener implementation for the EditText that contains the date of birth
     *
     * @param editText the actual EditText object that needs a listener
     * @param caller AnswerSheetActivity as reference to invoke functions from that class
     */
    private fun listen(editText: EditText, caller: AnswerSheetActivity) {
        editText.setOnClickListener {
            val calendarDate = Calendar.getInstance()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                datePickerDialog = DatePickerDialog(caller, R.style.DatePickerDialogTheme)
                datePickerDialog.setOnDateSetListener { _, year, month, day ->
                    calendarDate.set(Calendar.YEAR, year)
                    calendarDate.set(Calendar.MONTH, month)
                    calendarDate.set(Calendar.DAY_OF_MONTH, day)
                    updateLabel(editText, calendarDate)
                }
                datePickerDialog.show()
            } else {
                datePickerDialog = DatePickerDialog(
                    caller,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        calendarDate.set(Calendar.MONTH, monthOfYear)
                        calendarDate.set(Calendar.YEAR, year)
                        calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateLabel(editText, calendarDate)
                    },
                    calendarDate.get(Calendar.YEAR),
                    calendarDate.get(Calendar.MONTH),
                    calendarDate.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            }
        }
    }

    /**
     * Update function that changes the EditText fetching the date of birth and transforming the
     * Calendar object retrieved from the DatePickerDialog into the format "yyyy-MM-dd"
     *
     * @param editText the EditText that shall be modified
     * @param myCalendar Calendar object result from the DatePickerDialog
     */
    private fun updateLabel(editText: EditText, myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.GERMAN)
        editText.setText(sdf.format(myCalendar.time))
        selectedValue = editText.text.toString()
        timestamp = System.currentTimeMillis() / 1000L
    }
}