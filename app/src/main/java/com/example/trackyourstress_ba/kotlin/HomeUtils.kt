package com.example.trackyourstress_ba.kotlin

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache
import com.example.trackyourstress_ba.ui.home.HomeActivity
import java.util.*
import kotlin.random.Random

class HomeUtils {
    var requestQueue: RequestQueue
    lateinit var notificationUtils: NotificationUtils
    lateinit var tokenUtils: TokenUtils

    init {
        val cache = NoCache()
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
    }

    fun initiateTokenRefresher(activity: HomeActivity) {
        tokenUtils = TokenUtils()
        tokenUtils.scheduleTokenRefresher(activity)

    }

    fun initiateScheduling(activity: HomeActivity, notifications: ArrayList<Boolean>) {
        notificationUtils = NotificationUtils()
        val now = Calendar.getInstance()
        var nextDailyNotification = 0L
        var nextWeeklyNotification = 0L
        var nextMonthlyNotification = 0L

        if (notifications[0]) {
            nextDailyNotification = getRandomMilliSecondDaily()
        }
        val nextDaily = now.timeInMillis + nextDailyNotification
        notificationUtils.scheduleNotification(
            notificationUtils.getNotification(
                "Täglicher Fragebogen",
                activity
            ), nextDaily, activity
        )

        if (notifications[1]) {
            nextWeeklyNotification = getRandomMilliSecondWeekly()
        }
        val nextWeekly = now.timeInMillis + nextWeeklyNotification
        notificationUtils.scheduleNotification(
            notificationUtils.getNotification(
                "Wöchentlicher Fragebogen",
                activity
            ), nextWeekly, activity
        )

        if (notifications[2]) {
            nextMonthlyNotification = getRandomMilliSecondMonthly()
        }
        val nextMonthly = now.timeInMillis + nextMonthlyNotification
        notificationUtils.scheduleNotification(
            notificationUtils.getNotification(
                "Monatlicher Fragebogen",
                activity
            ), nextMonthly, activity
        )


    }

    private fun getRandomMilliSecondDaily(): Long {
        val milliSecondsDay = 60 * 60 * 24 * 1000
        return Random.nextLong((0L until milliSecondsDay + 1).random())
    }

    private fun getRandomMilliSecondWeekly(): Long {
        val milliSecondsWeek = 60 * 60 * 24 * 1000 * 7
        return Random.nextLong((0L until milliSecondsWeek + 1).random())
    }

    private fun getRandomMilliSecondMonthly(): Long {
        val milliSecondsMonth = 60 * 60 * 24 * 1000 * 30
        return Random.nextLong((0L until milliSecondsMonth + 1).random())
    }

    fun initiateNotificationSettings(caller: HomeActivity) {
        val preferences = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        preferences.edit().putBoolean("dailyNotification", true).apply()
        preferences.edit().putBoolean("weeklyNotification", true).apply()
        preferences.edit().putBoolean("monthlyNotification", true).apply()
    }

    fun getNotificationSettings(caller: HomeActivity): ArrayList<Boolean> {
        val preferences = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val notifications = ArrayList<Boolean>(3)
        if (preferences.contains("dailyNotification")) {
            notifications[0] = preferences.getBoolean("dailyNotification", true)
        }
        if (preferences.contains("weeklyNotification")) {
            notifications[1] = preferences.getBoolean("weeklyNotification", true)
        }
        if (preferences.contains("monthlyNotification")) {
            notifications[2] = preferences.getBoolean("monthlyNotification", true)
        }
        return notifications

    }
}