package com.example.trackyourstress_ba.NotificationManagement

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.app.NotificationManagerCompat


class NotificationPublisher : BroadcastReceiver() {
    companion object {
        var NOTIFICATION_ID = "notification_id"
        var NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences = context.getSharedPreferences(
            context.packageName, Context.MODE_PRIVATE
        )
        val notification = intent.getParcelableExtra<Parcelable>(NOTIFICATION) as Notification
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        when {
            notificationId == 100 && sharedPreferences.getBoolean("dailyNotification", true) -> {
                with(NotificationManagerCompat.from(context)) {
                    notify(notificationId, notification)
                }
            }
            notificationId == 101 && sharedPreferences.getBoolean(
                "weeklyNotification",
                true
            ) -> {
                with(NotificationManagerCompat.from(context)) {
                    notify(notificationId, notification)
                }
            }
            notificationId == 102 && sharedPreferences.getBoolean(
                "monthlyNotification",
                true
            ) -> {
                with(NotificationManagerCompat.from(context)) {
                    notify(notificationId, notification)
                }
            }
        }
    }
}