package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.HomeUtils
import org.json.JSONObject

class ActivitiesFragment : Fragment() {

    lateinit var root: LinearLayout
    lateinit var currentContext: Context
    //lateinit var

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_activities, container, false)
        currentContext = view!!.context
        root = view!!.findViewById(R.id.activitiesRootLayout)
        return view
    }

    override fun onStart() {
        super.onStart()
    }


    fun activitiesReceived(response: JSONObject) {
        val headline = TextView(currentContext)
        headline.text = getString(R.string.myActivities)
        root.addView(headline)
        val entries = response.getJSONArray("data")
        for (i in 0 until entries.length()) {
            val entry = entries.getJSONObject(i)
            val date =
                entry.getJSONObject("attributes").getJSONObject("created_at").getString("date")
                    .substring(0, 17)
            val message = entry.getJSONObject("attributes").getString("text")
            makeActivity(date, message)
        }
    }

    fun makeActivity(date: String, message: String) {
        val newMessage = TextView(currentContext)
        newMessage.text = "$date : $message"
        root.addView(newMessage)
    }



}