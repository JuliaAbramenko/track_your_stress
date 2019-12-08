package com.example.trackyourstress_ba.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_study_overview.*

class StudyOverviewFragment : Fragment() {

    private lateinit var table_studies: TableLayout
    val columns = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_study_overview, container, false)
        table_studies = view!!.findViewById(R.id.study_table)
        val first_row = TableRow(activity)
        val test_title = TextView(activity)
        val test_running = TextView(activity)
        val test_repeatable = TextView(activity)
        first_row.layoutParams =
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        test_title.text = getString(R.string.title_study)
        test_running.text = getString(R.string.running)
        test_repeatable.text = getString(R.string.repeatable)
        first_row.addView(test_title)
        first_row.addView(test_repeatable)
        first_row.addView(test_running)
        table_studies.addView(first_row)

        return view
    }

}