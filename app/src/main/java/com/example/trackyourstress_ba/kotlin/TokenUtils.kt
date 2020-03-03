package com.example.trackyourstress_ba.kotlin

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.job.JobInfo
import android.content.ComponentName
import android.app.job.JobScheduler
import com.example.trackyourstress_ba.ui.home.HomeActivity

class TokenUtils {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    fun scheduleTokenRefresher(activity: HomeActivity) {
        alarmMgr = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(activity, TokenReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(activity, 0, intent, 0)

        }
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            1000 * 60 * 30, //all 30 minutes refresh
            alarmIntent
        )
    }


}