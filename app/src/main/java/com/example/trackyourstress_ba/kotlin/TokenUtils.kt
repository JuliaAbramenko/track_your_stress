package com.example.trackyourstress_ba.kotlin

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.net.ConnectivityManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.trackyourstress_ba.ui.home.HomeActivity
import android.content.Intent
import android.util.Log


class TokenUtils {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    //private lateinit var tokenReceiver: TokenReceiver

    fun scheduleTokenRefresher(activity: HomeActivity) {
        val notificationIntent = Intent(activity, TokenReceiver::class.java)
        alarmMgr = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmUp = PendingIntent.getBroadcast(
            activity, 10,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        ) != null

        if (alarmUp) {
            Log.w("TokenUtils", "refresher is already active")
        } else {
            alarmIntent = PendingIntent.getBroadcast(
                activity, 10, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmMgr?.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis(),
                1000 * 60 * 30, //all 30 minutes refresh
                alarmIntent
            )
        }
    }


}
