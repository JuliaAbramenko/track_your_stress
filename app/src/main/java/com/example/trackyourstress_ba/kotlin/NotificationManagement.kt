package com.example.trackyourstress_ba.kotlin

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.home.HomeActivity
import kotlin.random.Random

class NotificationManagement(calling: HomeActivity) {
    var caller = calling

    fun initiateNotificationSettings(caller: HomeActivity) {
        val sharedPreferences = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putBoolean("dailyNotification", true).apply()
        sharedPreferences.edit().putBoolean("weeklyNotification", true).apply()
        sharedPreferences.edit().putBoolean("monthlyNotification", true).apply()
    }

    fun initiateScheduling(caller: HomeActivity) {
        val notifications = getNotificationSettings()
        val notificationUtils = NotificationUtils(caller)

        if (notifications[0]) {
            val nextDailyNotification = getRandomMilliSecondDaily()
            notificationUtils.scheduleNotification(
                notificationUtils.getNotification(
                    "Täglicher Fragebogen",
                    caller
                ), nextDailyNotification, 0, caller
            )
        }

        if (notifications[1]) {
            val nextWeeklyNotification = getRandomMilliSecondWeekly()
            notificationUtils.scheduleNotification(
                notificationUtils.getNotification(
                    "Wöchentlicher Fragebogen",
                    caller
                ), nextWeeklyNotification, 1, caller
            )
        }

        if (notifications[2]) {
            val nextMonthlyNotification = getRandomMilliSecondMonthly()
            notificationUtils.scheduleNotification(
                notificationUtils.getNotification(
                    "Monatlicher Fragebogen",
                    caller
                ), nextMonthlyNotification, 2, caller
            )
        }
    }


    private fun getNotificationSettings(): BooleanArray {
        val preferences = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val notifications = BooleanArray(3)
        if (preferences.contains("dailyNotification")) {
            notifications[0] = preferences.getBoolean("dailyNotification", false)
        }
        if (preferences.contains("weeklyNotification")) {
            notifications[1] = preferences.getBoolean("weeklyNotification", false)
        }
        if (preferences.contains("monthlyNotification")) {
            notifications[2] = preferences.getBoolean("monthlyNotification", false)
        }
        return notifications
    }

    private fun getRandomMilliSecondDaily(): Long {
        /*val milliSecondsDay = 60L * 60L * 24L * 1000L
        return Random.nextLong((0L until milliSecondsDay + 1).random())*/
        return 5000L
    }

    private fun getRandomMilliSecondWeekly(): Long {
        /*val milliSecondsWeek = 60L * 60L * 24L * 1000L * 7L
        return Random.nextLong((0L until milliSecondsWeek + 1).random())*/
        return 10000L
    }

    private fun getRandomMilliSecondMonthly(): Long {
        /*val milliSecondsMonth = 60L * 60L * 24L * 1000L * 30L
        return Random.nextLong((0L until milliSecondsMonth + 1).random())
    }*/
        return 20000L

    }

}

class NotificationUtils(calling: HomeActivity) {

    var caller = calling

    private lateinit var alarmManager: AlarmManager

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

    fun getNotification(content: String, caller: HomeActivity): Notification {
        val builder = NotificationCompat.Builder(caller, "999")
        val intent = Intent(caller, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(caller, 0, intent, 0)

        builder.setContentTitle("TrackYourStress")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_notifications_24dp)
        builder.setContentIntent(pendingIntent)
        return builder.build()
    }

    private fun createChannel(activity: HomeActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notifications"
            val descriptionText = "NotificationChannel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("0", name, importance)
            mChannel.description = descriptionText
            val notificationManager =
                activity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }
}
