package com.example.trackyourstress_ba.kotlin

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class NotificationPublisher : BroadcastReceiver() {
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        var notificationID = "notificationID"
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