package com.example.trackyourstress_ba.kotlin

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.R
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import com.example.trackyourstress_ba.LaunchActivity
import com.example.trackyourstress_ba.MainActivity



class NotificationPublisher : BroadcastReceiver() {
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