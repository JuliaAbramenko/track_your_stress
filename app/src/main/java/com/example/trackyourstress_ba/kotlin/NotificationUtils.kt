package com.example.trackyourstress_ba.kotlin

import android.content.Context
import com.example.trackyourstress_ba.ui.home.HomeActivity
import android.os.SystemClock
import android.content.Intent
import android.R
import android.app.*
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build


@Suppress("DEPRECATION")
class NotificationUtils {

    lateinit var alarmManager: AlarmManager

    fun scheduleNotification(
        notification: Notification,
        delay: Long,
        id: Int,
        activity: HomeActivity
    ) {
        val notificationIntent = Intent(activity, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.notificationID, id)
        notificationIntent.putExtra(NotificationPublisher.notification, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            id,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val futureInMillis = System.currentTimeMillis() + delay
        alarmManager = activity.getSystemService(Context.ALARM_SERVICE)!! as AlarmManager
        alarmManager.set(AlarmManager.RTC, futureInMillis, pendingIntent)

    }

    fun getNotification(content: String, activity: HomeActivity): Notification {
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(activity)
            Notification.Builder(activity, "TrackYourStress")
        } else {
            Notification.Builder(activity)
        }

        builder.setContentTitle("TrackYourStress")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_lock_idle_alarm)
        //builder.setFullScreenIntent()
        return builder.build()
    }

    private fun createChannel(activity: HomeActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test"
            val descriptionText = "TestChannel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("0", name, importance)
            mChannel.description = descriptionText
            val notificationManager =
                activity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }


}