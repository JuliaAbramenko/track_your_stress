package com.example.trackyourstress_ba.kotlin

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat


class OLDNotificationPublisher : BroadcastReceiver() {
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        var notificationID = "notificationID"
        var notification = "notification"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

}