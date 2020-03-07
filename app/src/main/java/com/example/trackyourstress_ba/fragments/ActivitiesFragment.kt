package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ActivitiesUtils
import com.example.trackyourstress_ba.kotlin.HomeUtils
import org.json.JSONObject
import org.w3c.dom.Text

class ActivitiesFragment : Fragment() {

    lateinit var root: LinearLayout
    lateinit var currentContext: Context
    lateinit var activitiesUtils: ActivitiesUtils
    lateinit var sharedPreferences: SharedPreferences
    var pages = 1
    var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_activities, container, false)
        currentContext = view!!.context
        sharedPreferences = currentContext.getSharedPreferences(
            currentContext.packageName, Context.MODE_PRIVATE
        )
        root = view.findViewById(R.id.activitiesRootLayout)
        return view
    }

    override fun onStart() {
        super.onStart()
        activitiesUtils = ActivitiesUtils()
        activitiesUtils.getActivities(1, this)

    }


    fun activitiesReceived(page: Int, response: JSONObject) {
        pages = response.getJSONObject("meta").getJSONObject("pagination").getString("total_pages")
            .toInt()
        currentPage = page
        val newContext = ContextThemeWrapper(currentContext, R.style.textViewStyleTYS)
        val headline = TextView(newContext)
        headline.text = getString(R.string.myActivities)
        headline.textSize = 24F
        root.addView(headline)
        val entries = response.getJSONArray("data")
        for (i in 0 until entries.length()) {
            val entry = entries.getJSONObject(i)
            val date =
                entry.getJSONObject("attributes").getJSONObject("created_at").getString("date")
                    .substring(0, 17)
            var message = entry.getJSONObject("attributes").getString("text")
            val potentialName = entry.getJSONObject("attributes").getString("name")
            if (potentialName != "null") {
                message = message.replace("{{name}}", potentialName, false)
            }
            makeActivity(date, message)
        }
        addButtons()
    }

    private fun makeActivity(date: String, message: String) {
        val newMessage = TextView(currentContext)
        newMessage.text = "$date : $message"
        root.addView(newMessage)
    }

    private fun addButtons() {
        if (pages > 1) {
            val newContext = ContextThemeWrapper(
                context,
                R.style.buttonStyleTYS
            )
            val buttonForward = Button(newContext, null, R.style.buttonStyleTYS)
            val buttonBack = Button(newContext, null, R.style.buttonStyleTYS)
            val relativeLayout = RelativeLayout(currentContext)
            val paramsLeft: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)
            val paramsRight: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            buttonBack.text = "Zurück"
            buttonForward.text = "Weiter"
            buttonForward.layoutParams = paramsRight
            buttonBack.layoutParams = paramsLeft
            relativeLayout.addView(buttonBack)
            relativeLayout.addView(buttonForward)
            root.addView(relativeLayout)
            listenBack(buttonBack)
            listenForward(buttonForward)
        }
    }

    fun retrievalFailed() {
        Toast.makeText(
            currentContext,
            "Aktivitäten konnten nicht geladen werden",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun listenBack(button: Button) {
        if (currentPage == 1) {
            button.background.setColorFilter(
                ContextCompat.getColor(
                    currentContext,
                    R.color.colorAccent
                ), PorterDuff.Mode.SRC_ATOP
            )
        }
        if (currentPage != 1) {
            button.background.setColorFilter(
                ContextCompat.getColor(
                    currentContext,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_ATOP
            )
        }
        button.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                clearView()
                activitiesUtils.getActivities(currentPage, this)
            }
        }
    }

    private fun listenForward(button: Button) {
        if (currentPage == pages) {
            button.background.setColorFilter(
                ContextCompat.getColor(
                    currentContext,
                    R.color.colorAccent
                ), PorterDuff.Mode.SRC_ATOP
            )
        }
        if (currentPage != pages) {

            button.background.setColorFilter(
                ContextCompat.getColor(
                    currentContext,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_ATOP
            )
        }
        button.setOnClickListener {
            if (currentPage != pages) {
                currentPage += 1
                clearView()
                activitiesUtils.getActivities(currentPage, this)
            }
        }
    }

    private fun clearView() {
        root.removeAllViews()
    }
}