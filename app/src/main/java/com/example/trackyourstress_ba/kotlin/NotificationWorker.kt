package com.example.trackyourstress_ba.kotlin

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*
import kotlin.random.Random
import android.app.AlarmManager
import android.os.SystemClock
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.content.SharedPreferences
import android.graphics.drawable.BitmapDrawable
import androidx.core.app.NotificationCompat
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.home.HomeActivity

 class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

     companion object {
         lateinit var notificationSettings: BooleanArray
     }

    val currentContext = appContext
     val sharedPreferences: SharedPreferences = currentContext.getSharedPreferences(
         currentContext.packageName, Context.MODE_PRIVATE
     )

     //TODO first time?
     override fun doWork(): Result {

         notificationSettings =
             if (!sharedPreferences.contains("nextDailyNotification") && !sharedPreferences.contains(
                     "nextWeeklyNotification"
                 ) && !sharedPreferences.contains("nextMonthlyNotification")
             ) {
                 initiateNotificationSettings()
                 getNotificationSettings()
             } else {
                 getNotificationSettings()
             }


        val date = Calendar.getInstance()
         if (notificationSettings[0]) {
             if (sharedPreferences.getLong(
                     "nextDailyNotification",
                     0
                 ) > date.timeInMillis || !sharedPreferences.contains("nextDailyNotification")
             ) {
                 createNewDailyNotification()
             }
         }

        val isNewWeek = date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
         if (isNewWeek && notificationSettings[1]) {
             if (sharedPreferences.getLong(
                     "nextWeeklyNotification",
                     0
                 ) > date.timeInMillis || !sharedPreferences.contains(("nextWeeklyNotification"))
             ) {
                 createNewWeeklyNotification()
             }
         }

        val isNewMonth = date.get(Calendar.DAY_OF_MONTH) == 1
         sharedPreferences.edit().putInt("currentMonth", date.get(Calendar.MONTH)).apply()

         val daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH)
         if (isNewMonth && notificationSettings[2]) {
             if (sharedPreferences.getLong(
                     "nextMonthlyNotification",
                     0
                 ) > date.timeInMillis || !sharedPreferences.contains("nextMonthlyNotification")
             ) {
                 createNewMonthlyNotification(daysInMonth)
             }
         }
        return Result.success()
    }

     private fun initiateNotificationSettings() {
         sharedPreferences.edit().putBoolean("dailyNotification", true).apply()
         sharedPreferences.edit().putBoolean("weeklyNotification", true).apply()
         sharedPreferences.edit().putBoolean("monthlyNotification", true).apply()
     }

     private fun createNewDailyNotification() {
         val nextDailyNotificationIn = getRandomMilliSecondDaily()
         scheduleNotification(
             applicationContext,
             "Es ist Zeit für einen täglichen Fragebogen!",
             nextDailyNotificationIn,
             100
         )
         sharedPreferences.edit().putLong("nextDailyNotification", nextDailyNotificationIn).apply()
     }

     private fun createNewWeeklyNotification() {
         val nextWeeklyNotificationIn = getRandomMilliSecondWeekly()
         scheduleNotification(
             applicationContext,
             "Es ist Zeit für einen wöchentlichen Fragebogen!",
             nextWeeklyNotificationIn,
             101
         )
         sharedPreferences.edit().putLong("nextWeeklyNotification", nextWeeklyNotificationIn)
             .apply()
     }

     private fun createNewMonthlyNotification(daysInMonth: Int) {
         val nextMonthlyNotificationIn = getRandomMilliSecondMonthly(daysInMonth)
         scheduleNotification(
             applicationContext,
             "Es ist Zeit für einen monatlichen Fragebogen!",
             nextMonthlyNotificationIn,
             102
         )
         sharedPreferences.edit().putLong("nextMonthlyNotification", nextMonthlyNotificationIn)
             .apply()
    }

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

    private fun getRandomMilliSecondDaily(): Long {
        val milliSecondsDay = 60 * 1000L//60L * 60L * 24L * 1000L
        return Random.nextLong((0L until milliSecondsDay + 1).random())
    }

    private fun getRandomMilliSecondWeekly(): Long {
        val milliSecondsWeek = 120 * 1000L//60L * 60L * 24L * 1000L * 7L
        return Random.nextLong((0L until milliSecondsWeek + 1).random())
    }

     private fun getRandomMilliSecondMonthly(daysInMonth: Int): Long {

         val milliSecondsMonth = 180 * 1000L//60L * 60L * 24L * 1000L * daysInMonth
        return Random.nextLong((0L until milliSecondsMonth + 1).random())
    }

     private fun scheduleNotification(
         context: Context,
         text: String,
         delay: Long,
         notificationId: Int
     ) {
         val builder = NotificationCompat.Builder(context)
             .setContentTitle("TrackYourStress")
             .setContentText(text)
             .setAutoCancel(true)
             .setSmallIcon(R.drawable.ic_notifications_24dp)
         //.setLargeIcon((context.resources.getDrawable(R.drawable.ic_questionnaire_24dp) as BitmapDrawable).bitmap)
         //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

         val intent = Intent(context, HomeActivity::class.java)
         val activity = PendingIntent.getActivity(
             context,
             notificationId,
             intent,
             PendingIntent.FLAG_CANCEL_CURRENT //cancel?
         )
         builder.setContentIntent(activity)

         val notification = builder.build()

         val notificationIntent = Intent(context, NotificationPublisher::class.java)
         notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId)
         notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
         val pendingIntent = PendingIntent.getBroadcast(
             context,
             notificationId,
             notificationIntent,
             PendingIntent.FLAG_CANCEL_CURRENT //cancel?
         )

         val futureInMillis = SystemClock.elapsedRealtime() + delay
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent)
     }


 }
