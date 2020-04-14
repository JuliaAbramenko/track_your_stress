package com.example.trackyourstress_ba

import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.example.trackyourstress_ba.kotlin.ClearingUtils
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.junit.Rule
import org.junit.Test

class SharedPreferencesTestStart {

    @get:Rule
    var activityTestRule: IntentsTestRule<LoginActivity> = IntentsTestRule(
        LoginActivity::class.java
    )

    @Test
    fun areSharedPrefsEmpty() {
        val activity = activityTestRule.activity
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        ClearingUtils.logoutUser(activity)
        assert(sharedPreferences == null)
    }

    @Test
    fun tokenAfterLoginSaved() {
        val conUtils = ConnectionUtils()
        val activity = activityTestRule.activity
        conUtils.loginUser("julia.abramenko@web.de", "testtest", activity)
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        assert(sharedPreferences.contains("token"))
    }
}