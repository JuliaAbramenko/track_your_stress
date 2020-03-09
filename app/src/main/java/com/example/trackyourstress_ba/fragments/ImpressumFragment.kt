package com.example.trackyourstress_ba.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R

class ImpressumFragment : Fragment() {
    lateinit var currentContext: Context
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_impressum, container, false)
        currentContext = container!!.context
        return view
    }

    override fun onStart() {
        super.onStart()
        val textView = TextView(currentContext)
        val text = currentContext.getString(R.string.impressum_text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(text, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
        } else {
            textView.text = text
        }
        val root = view!!.findViewById<LinearLayout>(R.id.impressum_root)
        root.addView(textView)
    }
}

