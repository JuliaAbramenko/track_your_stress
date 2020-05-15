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

/**
 * The class managing the ImprintFragment in the navigation drawer
 */
class ImprintFragment : Fragment() {
    lateinit var currentContext: Context
    /**
     * general creation method for the ImprintFragment. Is called before it is displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_impressum, container, false)
        this.activity?.title = getString(R.string.imprint)
        currentContext = container!!.context
        return view
    }

    /**
     * The actual creation in a GUI. As no associated layout xml file exists, only a TextView with a String in HTML format is
     * displayed from the underlying language resource file.
     */
    override fun onStart() {
        super.onStart()
        val textView = TextView(currentContext)
        val text = currentContext.getString(R.string.impressum_text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(text, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
        } else {
            textView.text = text
        }
        val root = requireView().findViewById<LinearLayout>(R.id.impressum_root)
        root.addView(textView)
    }
}

