package com.example.trackyourstress_ba.kotlin

import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import com.example.trackyourstress_ba.ui.home.HomeActivity
import androidx.core.content.ContextCompat.getSystemService
import android.R.attr.delay
import android.os.SystemClock
import android.content.Intent
import android.R
import android.app.*
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.trackyourstress_ba.ui.login.LoginActivity


class NotificationUtils() {

    lateinit var alarmManager: AlarmManager

    fun scheduleNotification(notification: Notification, delay: Int, activity: LoginActivity) {
        val notificationIntent = Intent(activity, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.notificationID, 1)
        notificationIntent.putExtra(NotificationPublisher.notification, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val futureInMillis = SystemClock.elapsedRealtime() + delay
        alarmManager = activity.getSystemService(Context.ALARM_SERVICE)!! as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)

    }

    fun getNotification(content: String, activity: LoginActivity): Notification {
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeChannel(activity)
            Notification.Builder(activity, "TrackYourStress")
        } else {
            Notification.Builder(activity)
        }

        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_lock_idle_alarm)
        return builder.build()
    }

    fun makeChannel(activity: LoginActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Test"
            val descriptionText = "TestChannel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("TrackYourStress", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                activity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }


}