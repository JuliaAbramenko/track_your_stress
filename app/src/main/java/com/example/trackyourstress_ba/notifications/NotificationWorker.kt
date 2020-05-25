package com.example.trackyourstress_ba.notifications

import android.app.*
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*
import kotlin.random.Random
import android.os.SystemClock
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.trackyourstress_ba.ui.start.LaunchActivity
import com.example.trackyourstress_ba.R

/**
 * Implementation of a Worker. Creates a NotificationWorker that will perform its task periodically in the
 * HomeActivity.
 *
 * @constructor
 *
 * @param appContext the current context (information about application environment)
 * @param workerParams setup parameters for a ListenableWorker
 */
class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        lateinit var notificationSettings: BooleanArray
    }

    val currentContext = appContext
    val sharedPreferences: SharedPreferences = currentContext.getSharedPreferences(
        currentContext.packageName, Context.MODE_PRIVATE
    )

    /**
     * Actual task that is executed whenever the NotificationWorker is triggered
     *
     * @return Result object that holds information whether the task succeeded
     */
    override fun doWork(): Result {
        notificationSettings =
            if (!sharedPreferences.contains("dailyNotification") && !sharedPreferences.contains(
                    "weeklyNotification"
                ) && !sharedPreferences.contains("monthlyNotification")
            ) {
                initiateNotificationSettings()
                getNotificationSettings()
            } else {
                getNotificationSettings()
            }

        val date = Calendar.getInstance()
        if (notificationSettings[0]) {
            Log.d("NotificationWorker", "Daily Notification will be scheduled")
            createNewDailyNotification()
        }

        val isNewWeek = date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
        if (isNewWeek && notificationSettings[1]) {
            Log.d("NotificationWorker", "Weekly Notification will be scheduled")
            createNewWeeklyNotification()
        }

        val isNewMonth = date.get(Calendar.DAY_OF_MONTH) == 1
        val daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (isNewMonth && notificationSettings[2]) {
            Log.d("NotificationWorker", "Monthly Notification will be scheduled")
            createNewMonthlyNotification(daysInMonth)
        }
        return Result.success()
    }

    /**
     * Initialization method to store all types of notifications that are wished by the user.
     * First all are set to true.
     *
     */
    private fun initiateNotificationSettings() {
        sharedPreferences.edit().putBoolean("dailyNotification", true).apply()
        sharedPreferences.edit().putBoolean("weeklyNotification", true).apply()
        sharedPreferences.edit().putBoolean("monthlyNotification", true).apply()
    }

    /**
     * Function is invoked every day. It schedules a daily notification with a random time to display
     *
     */
    private fun createNewDailyNotification() {
        val nextDailyNotificationIn = getRandomMilliSecondDaily()
        scheduleNotification(
            applicationContext,
            currentContext.getString(R.string.time_daily_questionnaire),
            nextDailyNotificationIn,
            100,
            "10000"
        )
    }

    /**
     * Function is invoked every monday. It schedules a weekly notification with a random time to display
     *
     */
    private fun createNewWeeklyNotification() {
        val nextWeeklyNotificationIn = getRandomMilliSecondWeekly()
        scheduleNotification(
            applicationContext,
            currentContext.getString(R.string.time_weekly_questionnaire),
            nextWeeklyNotificationIn,
            101,
            "10001"
        )
    }

    /**
     * Function is invoked every first day of the month. It schedules a monthly notification with a random time to display
     *
     */
    private fun createNewMonthlyNotification(daysInMonth: Int) {
        val nextMonthlyNotificationIn = getRandomMilliSecondMonthly(daysInMonth)
        scheduleNotification(
            applicationContext,
            currentContext.getString(R.string.time_monthly_questionnaire),
            nextMonthlyNotificationIn,
            102,
            "10002"
        )
    }

    /**
     * Method to retrieve the current notification settings from the SharedPreferences
     *
     * @return a BooleanArray with all three values or daily, weekly and monthly notifications
     */
    private fun getNotificationSettings(): BooleanArray {
        val notifications = BooleanArray(3)
        if (sharedPreferences.contains("dailyNotification")) {
            notifications[0] = sharedPreferences.getBoolean("dailyNotification", false)
        }
        if (sharedPreferences.contains("weeklyNotification")) {
            notifications[1] = sharedPreferences.getBoolean("weeklyNotification", false)
        }
        if (sharedPreferences.contains("monthlyNotification")) {
            notifications[2] = sharedPreferences.getBoolean("monthlyNotification", false)
        }
        return notifications
    }

    /**
     * Generates a random number between 0 and the amount of milliseconds a day has.
     * Uses it then as delay for a daily notification scheduling
     *
     * @return a random number between 0 and the amount of milliseconds a day has
     */
    private fun getRandomMilliSecondDaily(): Long {
        val milliSecondsDay = 60L * 60L * 24L * 1000L
        return Random.nextLong((0L until milliSecondsDay + 1).random())
    }

    /**
     * Generates a random number between 0 and the amount of milliseconds a week has.
     * Uses it then as delay for a weekly notification scheduling
     *
     * @return a random number between 0 and the amount of milliseconds a week has
     */
    private fun getRandomMilliSecondWeekly(): Long {
        val milliSecondsWeek = 60L * 60L * 24L * 1000L * 7L
        return Random.nextLong((0L until milliSecondsWeek + 1).random())
    }

    /**
     * Generates a random number between 0 and the amount of milliseconds the current month has.
     * Uses it then as delay for a monthly notification scheduling
     *
     * @param daysInMonth parameter included into the calculation. Varies from month to month.
     * @return a random number between 0 and the amount of milliseconds the month has
     */
    private fun getRandomMilliSecondMonthly(daysInMonth: Int): Long {
        val milliSecondsMonth = 60L * 60L * 24L * 1000L * daysInMonth
        return Random.nextLong((0L until milliSecondsMonth + 1).random())
    }

    /**
     * Actual scheduling method that uses an AlarmManager to set the alarm for a Notification
     *
     * @param context current information about application environment
     * @param text the text that shall be displayed in the notification. Depends on which type (daily,
     * weekly, monthly).
     * @param delay previously generated delay for an alarm
     * @param notificationId unique representation for a notification by id.
     * 100 = daily
     * 101 = weekly
     * 102 = monthly
     * @param channelId unique representation for a notification channel with an id.
     * "10000" = daily channel
     * "10001" = weekly channel
     * "10002" = monthly channel
     */
    private fun scheduleNotification(
        context: Context,
        text: String,
        delay: Long,
        notificationId: Int,
        channelId: String
    ) {
        val notifyIntent = Intent(context, LaunchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 1111, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val futureInMillis = SystemClock.elapsedRealtime() + delay

        val builder = NotificationCompat.Builder(currentContext, channelId)
            .setSmallIcon(R.drawable.ic_notifications_24dp)
            .setContentTitle("TrackYourStress")
            .setContentText(text)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(notifyPendingIntent)
            .setWhen(System.currentTimeMillis() + delay)

        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, builder.build()!!)
        Log.d("NotificationManager", "Intent set!")
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TrackYourStress - NotificationManager"
            val descriptionText = "Notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    }
}
