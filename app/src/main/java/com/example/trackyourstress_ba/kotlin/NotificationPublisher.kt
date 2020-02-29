package com.example.trackyourstress_ba.kotlin

import android.app.Notification
import android.content.BroadcastReceiver
import android.app.NotificationManager
import android.content.Context
import android.content.Intent


class NotificationPublisher : BroadcastReceiver() {
    companion object {
        var notificationID = "notification-id"
        var notification = "notification"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = intent.getParcelableExtra<Notification>(notification)
        val id = intent.getIntExtra(notificationID, 0)
        notificationManager.notify(id, notification)

    }
}