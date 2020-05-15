package com.example.trackyourstress_ba.notifications

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.app.NotificationManagerCompat

/**
 * Implementation of the BroadcastReceiver that is used to deliver an notification.
 *
 */
class NotificationPublisher : BroadcastReceiver() {
    companion object {
        var NOTIFICATION_ID = "notification_id"
        var NOTIFICATION = "notification"
    }

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast - in this case, the AlarmManager
     * wants to deliver a Notification.
     * Verifies if the user still wishes to see the notification based on the boolean values for notification
     * types stored in the SharedPreferences
     *
     * @param context the current context (information about application environment)
     * @param intent the Intent from which the request is coming (PendingIntent from the AlarmManager)
     */
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