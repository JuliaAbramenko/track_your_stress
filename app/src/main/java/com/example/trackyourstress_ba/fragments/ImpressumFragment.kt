package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
        val text = requireActivity().getString(R.string.impressumText)
        textView.text = Html.fromHtml(text)
        val root = requireActivity().findViewById<LinearLayout>(R.id.impressum_root)
        root.addView(textView)
    }
}

