package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.utility.ActivitiesUtils
import org.json.JSONObject

@Suppress("DEPRECATION")
/**
 * The class managing the ActivitiesFragment in the navigation drawer
 */
class ActivitiesFragment : Fragment() {
    private lateinit var root: LinearLayout
    lateinit var currentContext: Context
    private lateinit var activitiesUtils: ActivitiesUtils
    lateinit var sharedPreferences: SharedPreferences
    private var pages = 1
    private var currentPage = 1

    /**
     * general creation method for the ActivitiesFragment. Is called before it is displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_activities, container, false)
        this.activity?.title = getString(R.string.activities)
        currentContext = view!!.context
        sharedPreferences = currentContext.getSharedPreferences(
            currentContext.packageName, Context.MODE_PRIVATE
        )
        root = view.findViewById(R.id.activitiesRootLayout)
        return view
    }

    /**
     * Method invoked when View is displayed. Starting with activities from page 1.
     */
    override fun onStart() {
        super.onStart()
        val activity = this.activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.activities)
        activitiesUtils = ActivitiesUtils()
        activitiesUtils.getActivities(1, this)
    }

    /**
     * Invoked function when activities retrieval from the server went successfully. Relevant values to display are extracted.
     *
     * @param page for which distinct JSON is fetched
     * @param response actual fetched response
     */
    fun activitiesReceived(page: Int, response: JSONObject) {
        pages = response.getJSONObject("meta").getJSONObject("pagination").getString("total_pages")
            .toInt()
        currentPage = page
        val entries = response.getJSONArray("data")
        for (i in 0 until entries.length()) {
            val entry = entries.getJSONObject(i)
            val date =
                entry.getJSONObject("attributes").getJSONObject("created_at").getString("date")
                    .substring(0, 16)
            var message = entry.getJSONObject("attributes").getString("text")
            val potentialName = entry.getJSONObject("attributes").getString("name")
            if (potentialName != "null") {
                message = message.replace("{{name}}", potentialName, false)
            }
            makeActivity(date, message)
        }
        addButtons()
    }

    /**
     * Creation of an entry in the ActivitiesFragment with extracted relevant messages and date.
     *
     * @param date extracted date value
     * @param message extracted message string
     */
    private fun makeActivity(date: String, message: String) {
        val newMessage = TextView(currentContext)
        newMessage.text = "$date : $message"
        root.addView(newMessage)
    }

    /**
     * Handles button creation and positioning. Only adds buttons if at least 2 pages exist in total.
     */
    private fun addButtons() {
        if (pages > 1) {
            val newContext = ContextThemeWrapper(
                context,
                R.style.buttonStyleTYS
            )
            val buttonForward = Button(newContext, null, R.style.buttonStyleTYS)
            val font: Typeface? = ResourcesCompat.getFont(currentContext, R.font.alegreya_sc)
            buttonForward.typeface = font
            val buttonBack = Button(newContext, null, R.style.buttonStyleTYS)
            buttonBack.typeface = font
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
            buttonBack.text = getString(R.string.Back)
            buttonForward.text = getString(R.string.go_on)
            buttonForward.layoutParams = paramsRight
            buttonBack.layoutParams = paramsLeft
            relativeLayout.addView(buttonBack)
            relativeLayout.addView(buttonForward)
            root.addView(relativeLayout)
            listenBack(buttonBack)
            listenForward(buttonForward)
        }
    }

    /**
     * Function to display an server  error to the user.
     */
    fun retrievalFailed() {
        Toast.makeText(
            currentContext,
            getString(R.string.activities_not_loaded),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Back button color adaptions depending on the actual page number with a click listener for page adaptions.
     *
     * @param button back Button that is positioned left
     */
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

    /**
     * Forward button color adaptions depending on the actual page number with a click listener for page adaptions.
     *
     * @param button forward Button that is positioned right
     */
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

    /**
     * Method to delete the content currently displayed on the screen. Used whenever another page is called so the views do not overlap.
     */
    private fun clearView() {
        root.removeAllViews()
    }

    /**
     * Function to display an network error to the user.
     */
    fun notifyNetworkError() {
        Toast.makeText(
            currentContext,
            getString(R.string.network_error),
            Toast.LENGTH_LONG
        ).show()
    }
}