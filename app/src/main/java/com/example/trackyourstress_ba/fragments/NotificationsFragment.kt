package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R

class NotificationsFragment : Fragment() {

    lateinit var switchAllNotifications: Switch
    lateinit var switchDaily: Switch
    lateinit var switchWeekly: Switch
    lateinit var switchMonthly: Switch
    lateinit var currentContext: Context
    lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentContext = container!!.context
        this.activity?.title = "Benachrichtigungen"
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onStart() {
        super.onStart()
        switchAllNotifications = view!!.findViewById(R.id.switch_all_notifications)
        switchDaily = view!!.findViewById(R.id.switch_daily_notfications)
        switchWeekly = view!!.findViewById(R.id.switch_weekly_notifications)
        switchMonthly = view!!.findViewById(R.id.switch_monthly_notifications)

        preferences = currentContext.getSharedPreferences(
            currentContext.packageName, Context.MODE_PRIVATE
        )
        switchDaily.isChecked = preferences.getBoolean("dailyNotification", true)
        switchWeekly.isChecked = preferences.getBoolean("weeklyNotification", true)
        switchMonthly.isChecked = preferences.getBoolean("monthlyNotification", true)

        switchAllNotifications.isChecked =
            preferences.getBoolean("dailyNotification", true) &&
                    preferences.getBoolean("weeklyNotification", true) &&
                    preferences.getBoolean("monthlyNotification", true)

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

    }

    fun dailyTrue() {
        preferences.edit().putBoolean("dailyNotification", true).apply()
    }

    fun dailyFalse() {
        preferences.edit().putBoolean("dailyNotification", false).apply()
    }

    fun weeklyTrue() {
        preferences.edit().putBoolean("weeklyNotification", true).apply()
    }

    fun weeklyFalse() {
        preferences.edit().putBoolean("weeklyNotification", false).apply()
    }

    fun monthlyTrue() {
        preferences.edit().putBoolean("monthlyNotification", true).apply()
    }

    fun monthlyFalse() {
        preferences.edit().putBoolean("monthlyNotification", false).apply()
    }
}