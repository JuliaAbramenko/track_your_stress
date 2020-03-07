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

    fun initiateScheduling(activity: HomeActivity, notifications: BooleanArray) {
        notificationUtils = NotificationUtils()
        val now = Calendar.getInstance()

        if (notifications[0]) {
            var nextDailyNotification = getRandomMilliSecondDaily()
            val nextDaily = nextDailyNotification
            notificationUtils.scheduleNotification(
                notificationUtils.getNotification(
                    "Täglicher Fragebogen",
                    activity
                ), nextDaily, 0, activity
            )
        }

        if (notifications[1]) {
            var nextWeeklyNotification = getRandomMilliSecondWeekly()
            val nextWeekly = nextWeeklyNotification
            notificationUtils.scheduleNotification(
                notificationUtils.getNotification(
                    "Wöchentlicher Fragebogen",
                    activity
                ), nextWeekly, 1, activity
            )
        }

        if (notifications[2]) {
            var nextMonthlyNotification = getRandomMilliSecondMonthly()
            val nextMonthly = nextMonthlyNotification
            notificationUtils.scheduleNotification(
                notificationUtils.getNotification(
                    "Monatlicher Fragebogen",
                    activity
                ), nextMonthly, 2, activity
            )
        }
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

    fun initiateNotificationSettings(caller: HomeActivity) {
        val preferences = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        preferences.edit().putBoolean("dailyNotification", true).apply()
        preferences.edit().putBoolean("weeklyNotification", true).apply()
        preferences.edit().putBoolean("monthlyNotification", true).apply()
    }

    fun getNotificationSettings(caller: HomeActivity): BooleanArray {
        val preferences = caller.getSharedPreferences(
            caller.packageName, Context.MODE_PRIVATE
        )
        val notifications = BooleanArray(3)
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