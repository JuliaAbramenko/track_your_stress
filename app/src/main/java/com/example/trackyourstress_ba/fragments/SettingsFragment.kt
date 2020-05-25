package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

/**
 * The class managing the SettingsFragment in the navigation drawer
 */
@Suppress("DEPRECATION")
class SettingsFragment : Fragment() {

    private lateinit var switchAllNotifications: Switch
    private lateinit var switchDaily: Switch
    private lateinit var switchWeekly: Switch
    private lateinit var switchMonthly: Switch
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    /**
     * general creation method for the SettingsFragment. Is called before it is displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentContext = container!!.context
        this.activity?.title = getString(R.string.settings)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    /**
     * Called at the actual display of the View. There is an underlying layout xml "fragment_settings" where it is defined. All switches and flag ImageViews
     * have click listeners registered.
     */
    override fun onStart() {
        super.onStart()
        //this.activity?.actionBar!!.title = getString(R.string.settings)
        val activity = this.activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.settings)
        switchAllNotifications = requireView().findViewById(R.id.switch_all_notifications)
        switchDaily = requireView().findViewById(R.id.switch_daily_notfications)
        switchWeekly = requireView().findViewById(R.id.switch_weekly_notifications)
        switchMonthly = requireView().findViewById(R.id.switch_monthly_notifications)

        sharedPreferences = currentContext.getSharedPreferences(
            currentContext.packageName, Context.MODE_PRIVATE
        )
        switchDaily.isChecked = sharedPreferences.getBoolean("dailyNotification", true)
        switchWeekly.isChecked = sharedPreferences.getBoolean("weeklyNotification", true)
        switchMonthly.isChecked = sharedPreferences.getBoolean("monthlyNotification", true)

        switchAllNotifications.isChecked =
            sharedPreferences.getBoolean("dailyNotification", true) &&
                    sharedPreferences.getBoolean("weeklyNotification", true) &&
                    sharedPreferences.getBoolean("monthlyNotification", true)

        switchAllNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchDaily.isChecked = true
                switchDaily.text = getString(R.string.daily_notifications_on)
                setDaily(true)
                switchWeekly.isChecked = true
                switchWeekly.text = getString(R.string.weekly_notfications_on)
                setWeekly(true)
                switchMonthly.isChecked = true
                switchMonthly.text = getString(R.string.monthly_notifications_on)
                setMonthly(true)

            } else {
                switchDaily.isChecked = false
                switchDaily.text = getString(R.string.daily_notifications_off)
                setDaily(false)
                switchWeekly.isChecked = false
                switchWeekly.text = getString(R.string.weekly_notifications_off)
                setWeekly(false)
                switchMonthly.isChecked = false
                switchMonthly.text = getString(R.string.monthly_notifications_off)
                setMonthly(false)
            }
        }

        switchDaily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchDaily.text = getString(R.string.daily_notifications_on)
                setDaily(true)
            } else {
                switchDaily.text = getString(R.string.daily_notifications_off)
                setDaily(false)
                switchAllNotifications.isChecked = false
            }
        }

        switchWeekly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchWeekly.text = getString(R.string.weekly_notfications_on)
                setWeekly(true)
            } else {
                switchWeekly.text = getString(R.string.weekly_notifications_off)
                setWeekly(false)
                switchAllNotifications.isChecked = false
            }
        }

        switchMonthly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchMonthly.text = getString(R.string.monthly_notifications_on)
                setMonthly(true)
            } else {
                switchMonthly.text = getString(R.string.monthly_notifications_off)
                setMonthly(false)
                switchAllNotifications.isChecked = false
            }
        }

        val germanFlag = requireView().findViewById<ImageView>(R.id.germanFlag)
        germanFlag.setOnClickListener {
            sharedPreferences.edit().putString("locale", "de").apply()
            setLocale(Locale.GERMAN)
            updateText()
        }
        val englishFlag = requireView().findViewById<ImageView>(R.id.englishFlag)
        englishFlag.setOnClickListener {
            sharedPreferences.edit().putString("locale", "en").apply()
            setLocale(Locale.ENGLISH)
            updateText()
        }
    }
    /**
     * Setting of the app language to the specified locale
     */
    private fun setLocale(locale: Locale) {
        val config = Configuration(resources.configuration)
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * Method for updating the visible fields of the Fragment and Toolbar so that language changes are seen immediately.
     */
    private fun updateText() {
        switchAllNotifications.text = getString(R.string.turn_on_all_notifications)
        switchDaily.text = getString(R.string.turn_on_daily_notifications)
        switchWeekly.text = getString(R.string.turn_on_weekly_notifications)
        switchMonthly.text = getString(R.string.turn_on_monthly_notifications)
        val notificationTextView = view!!.findViewById<TextView>(R.id.manage_notifications_textview)
        notificationTextView.text = getString(R.string.manage_notifications)
        val languageTextView = view!!.findViewById<TextView>(R.id.manage_language)
        languageTextView.text = getString(R.string.language_settings)
        val activity = this.activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.settings)
        updateNavigationDrawer()
    }

    /**
     * Method for updating the text in the navigation drawer to display language changes immediately.
     */
    private fun updateNavigationDrawer() {
        val navView = this.activity?.nav_view
        navView?.menu?.getItem(0)?.title = getString(R.string.home)
        navView?.menu?.getItem(1)?.title = getString(R.string.profile)
        navView?.menu?.getItem(2)?.title = getString(R.string.activities)
        navView?.menu?.getItem(3)?.title = getString(R.string.questionnaires)
        navView?.menu?.getItem(4)?.title = getString(R.string.settings)
        navView?.menu?.getItem(5)?.title = getString(R.string.options)
        navView?.menu?.getItem(5)?.subMenu?.getItem(0)?.title = getString(R.string.imprint)
        navView?.menu?.getItem(5)?.subMenu?.getItem(1)?.title = getString(R.string.about_us)
    }

    /**
     * Change of "dailyNotification" in SharedPreferences via argument
     */
    private fun setDaily(value: Boolean) {
        sharedPreferences.edit().putBoolean("dailyNotification", value).apply()
    }

    /**
     * Change of "weeklyNotification" in SharedPreferences via argument
     */
    private fun setWeekly(value: Boolean) {
        sharedPreferences.edit().putBoolean("weeklyNotification", value).apply()
    }

    /**
     * Change of "monthlyNotification" in SharedPreferences via argument
     */
    private fun setMonthly(value: Boolean) {
        sharedPreferences.edit().putBoolean("monthlyNotification", value).apply()
    }

}