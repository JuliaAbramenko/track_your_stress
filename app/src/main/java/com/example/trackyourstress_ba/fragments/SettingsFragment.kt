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
                dailyTrue()
                switchWeekly.isChecked = true
                switchWeekly.text = getString(R.string.weekly_notfications_on)
                weeklyTrue()
                switchMonthly.isChecked = true
                switchMonthly.text = getString(R.string.monthly_notifications_on)
                monthlyTrue()

            } else {
                switchDaily.isChecked = false
                switchDaily.text = getString(R.string.daily_notifications_off)
                dailyFalse()
                switchWeekly.isChecked = false
                switchWeekly.text = getString(R.string.weekly_notifications_off)
                weeklyFalse()
                switchMonthly.isChecked = false
                switchMonthly.text = getString(R.string.monthly_notifications_off)
                monthlyFalse()
            }
        }

        switchDaily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchDaily.text = getString(R.string.daily_notifications_on)
                dailyTrue()
            } else {
                switchDaily.text = getString(R.string.daily_notifications_off)
                dailyFalse()
                switchAllNotifications.isChecked = false
            }
        }

        switchWeekly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchWeekly.text = getString(R.string.weekly_notfications_on)
                weeklyTrue()
            } else {
                switchWeekly.text = getString(R.string.weekly_notifications_off)
                weeklyFalse()
                switchAllNotifications.isChecked = false
            }
        }

        switchMonthly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchMonthly.text = getString(R.string.monthly_notifications_on)
                monthlyTrue()
            } else {
                switchMonthly.text = getString(R.string.monthly_notifications_off)
                monthlyFalse()
                switchAllNotifications.isChecked = false
            }
        }

        val germanFlag = requireView().findViewById<ImageView>(R.id.germanFlag)
        germanFlag.setOnClickListener {
            sharedPreferences.edit().putString("locale", "de").apply()
            setGermanLocale()
            updateText()
        }
        val englishFlag = requireView().findViewById<ImageView>(R.id.englishFlag)
        englishFlag.setOnClickListener {
            sharedPreferences.edit().putString("locale", "en").apply()
            setEnglishLocale()
            updateText()
        }
    }

    /**
     * Setting of the app language to German
     */
    private fun setGermanLocale() {
        val config = Configuration(resources.configuration)
        config.locale = Locale.GERMAN
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * Setting of the app language to English
     */
    private fun setEnglishLocale() {
        val config = Configuration(resources.configuration)
        config.locale = Locale.ENGLISH
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
     * Change of "dailyNotification" in SharedPreferences to true
     */
    private fun dailyTrue() {
        sharedPreferences.edit().putBoolean("dailyNotification", true).apply()
    }

    /**
     * Change of "dailyNotification" in SharedPreferences to false
     */
    private fun dailyFalse() {
        sharedPreferences.edit().putBoolean("dailyNotification", false).apply()
    }

    /**
     * Change of "weeklyNotification" in SharedPreferences to true
     */
    private fun weeklyTrue() {
        sharedPreferences.edit().putBoolean("weeklyNotification", true).apply()
    }

    /**
     * Change of "weeklyNotification" in SharedPreferences to false
     */
    private fun weeklyFalse() {
        sharedPreferences.edit().putBoolean("weeklyNotification", false).apply()
    }

    /**
     * Change of "monthlyNotification" in SharedPreferences to true
     */
    private fun monthlyTrue() {
        sharedPreferences.edit().putBoolean("monthlyNotification", true).apply()
    }

    /**
     * Change of "monthlyNotification" in SharedPreferences to false
     */
    private fun monthlyFalse() {
        sharedPreferences.edit().putBoolean("monthlyNotification", false).apply()
    }
}