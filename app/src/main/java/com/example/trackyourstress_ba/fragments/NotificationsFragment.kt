package com.example.trackyourstress_ba.fragments

import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import android.widget.Switch
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import kotlinx.android.synthetic.main.activity_home.*

class NotificationsFragment : Fragment() {

    lateinit var switch_all_notifications: Switch
    lateinit var switch_daily: Switch
    lateinit var switch_weekly: Switch
    lateinit var switch_monthly: Switch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onStart() {
        super.onStart()
        switch_all_notifications = view!!.findViewById(R.id.switch_all_notifications)
        switch_daily = view!!.findViewById(R.id.switch_daily_notfications)
        switch_weekly = view!!.findViewById(R.id.switch_weekly_notifications)
        switch_monthly = view!!.findViewById(R.id.switch_monthly_notifications)

        switch_all_notifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_daily.isChecked = true
                switch_daily.text = getString(R.string.daily_notifications_on)
                switch_weekly.isChecked = true
                switch_weekly.text = getString(R.string.weekly_notfications_on)
                switch_monthly.isChecked = true
                switch_monthly.text = getString(R.string.monthly_notifications_on)
            } else {
                switch_daily.isChecked = false
                switch_daily.text = getString(R.string.daily_notifications_off)
                switch_weekly.isChecked = false
                switch_weekly.text = getString(R.string.weekly_notifications_off)
                switch_monthly.isChecked = false
                switch_monthly.text = getString(R.string.monthly_notifications_off)
            }
        }

        switch_daily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_daily.text = getString(R.string.daily_notifications_on)
            } else {
                switch_daily.text = getString(R.string.daily_notifications_off)
            }
        }

        switch_weekly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_weekly.text = getString(R.string.weekly_notfications_on)
            } else {
                switch_weekly.text = getString(R.string.weekly_notifications_off)
            }
        }

        switch_monthly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_monthly.text = getString(R.string.monthly_notifications_on)
            } else {
                switch_monthly.text = getString(R.string.monthly_notifications_off)
            }
        }

    }
}