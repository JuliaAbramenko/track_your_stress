package com.example.trackyourstress_ba.kotlin

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Parcelable
import androidx.core.app.NotificationManagerCompat


class NotificationPublisher : BroadcastReceiver() {
    companion object {
        var NOTIFICATION_ID = "notification_id"
        var NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context, intent: Intent) {
        //val notificationManager =
        //   context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification = intent.getParcelableExtra<Parcelable>(NOTIFICATION) as Notification
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        //notificationManager.notify(notificationId, notification)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, notification)
        }
    }
}