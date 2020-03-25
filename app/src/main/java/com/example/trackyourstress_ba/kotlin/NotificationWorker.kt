package com.example.trackyourstress_ba.kotlin

import android.app.*
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*
import kotlin.random.Random
import android.os.SystemClock
import android.content.Intent
import android.media.RingtoneManager
import android.content.SharedPreferences
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
             /*if (sharedPreferences.getLong(
                     "nextDailyNotification",
                     0
                 ) > date.timeInMillis || !sharedPreferences.contains("nextDailyNotification")
             ) {
                 createNewDailyNotification()
             }*/
             createNewDailyNotification()
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
             800
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

         val notifyIntent = Intent(context, HomeActivity::class.java).apply {
             flags = Intent.FLAG_ACTIVITY_NEW_TASK
         }
         val notifyPendingIntent = PendingIntent.getActivity(
             context, 1111, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
         )

         val channelId = "333"
         val builder = NotificationCompat.Builder(currentContext, channelId)
             .setSmallIcon(R.drawable.ic_notifications_24dp)
             .setContentTitle("TrackYourStress")
             .setContentText(text)
             .setStyle(
                 NotificationCompat.BigTextStyle()
                     .bigText(text)
             )
             .setPriority(NotificationCompat.PRIORITY_DEFAULT)
             .setContentIntent(notifyPendingIntent)

         val notificationIntent = Intent(context, NotificationPublisher::class.java)
         notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId)
         notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, builder.build())
         val pendingIntent = PendingIntent.getBroadcast(
             context,
             notificationId,
             notificationIntent,
             PendingIntent.FLAG_CANCEL_CURRENT //cancel?
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

             //notificationManager.notify(notificationId, builder.build())
         } else {
             with(NotificationManagerCompat.from(context)) {
                 // notificationId is a unique int for each notification that you must define
                 //notify(notificationId, builder.build())
             }
         }
         val futureInMillis = SystemClock.elapsedRealtime() + delay
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent)
     }
 }
