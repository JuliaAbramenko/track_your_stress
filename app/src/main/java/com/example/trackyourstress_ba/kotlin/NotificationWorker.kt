package com.example.trackyourstress_ba.kotlin

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*
import kotlin.random.Random

//TODO
/*
 class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    val currentContext = appContext

    override fun doWork(): Result {

        val notificationSettings = getNotificationSettings()

        val date = Calendar.getInstance()
        //val isNewDay = date.get(Calendar.HOUR_OF_DAY) == 0
        createNewDailyNotification()
        val isNewWeek = date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
        createNewWeeklyNotification()
        val isNewMonth = date.get(Calendar.DAY_OF_MONTH) == 1
        createNewMonthlyNotification()

        return Result.success()
    }

    private fun createNewMonthlyNotification() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createNewWeeklyNotification() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createNewDailyNotification() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun getNotificationSettings(): BooleanArray {
        val preferences = currentContext.getSharedPreferences(
            currentContext.packageName, Context.MODE_PRIVATE
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
        val milliSecondsDay = 60L * 60L * 24L * 1000L
        return Random.nextLong((0L until milliSecondsDay + 1).random())
    }

    private fun getRandomMilliSecondWeekly(): Long {
        val milliSecondsWeek = 60L * 60L * 24L * 1000L * 7L
        return Random.nextLong((0L until milliSecondsWeek + 1).random())
    }

    private fun getRandomMilliSecondMonthly(): Long {
        val milliSecondsMonth = 60L * 60L * 24L * 1000L * 30L
        return Random.nextLong((0L until milliSecondsMonth + 1).random())
    }
}*/
