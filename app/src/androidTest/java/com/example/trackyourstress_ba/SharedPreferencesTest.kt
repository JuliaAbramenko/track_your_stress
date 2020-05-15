package com.example.trackyourstress_ba

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.example.trackyourstress_ba.Utils.ClearingUtils
import com.example.trackyourstress_ba.Utils.ConnectionUtils
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.junit.Test

class SharedPreferencesTest {

    @Test
    fun containsToken() {
        val loginActivityTest: IntentsTestRule<LoginActivity> =
            IntentsTestRule(LoginActivity::class.java)
        loginActivityTest.launchActivity(Intent())
        val activity = loginActivityTest.activity
        val conUtils = ConnectionUtils()
        conUtils.loginUser("julia.abramenko@web.de", "testtest", activity)
        Thread.sleep(2000)
        intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        assert(sharedPreferences.contains("token"))
    }

    @Test
    fun containsUserName() {
        val loginActivityTest: IntentsTestRule<LoginActivity> =
            IntentsTestRule(LoginActivity::class.java)
        loginActivityTest.launchActivity(Intent())
        val activity = loginActivityTest.activity
        val conUtils = ConnectionUtils()
        conUtils.loginUser("julia.abramenko@web.de", "testtest", activity)
        Thread.sleep(3000)
        intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        assert(sharedPreferences.contains("userName"))
    }

    @Test
    fun tokenAfterLogout() {
        val loginActivityTest: IntentsTestRule<LoginActivity> =
            IntentsTestRule(LoginActivity::class.java)
        loginActivityTest.launchActivity(Intent())
        val activity = loginActivityTest.activity
        val conUtils = ConnectionUtils()
        conUtils.loginUser("julia.abramenko@web.de", "testtest", activity)
        Thread.sleep(2000)
        intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        Thread.sleep(2000)
        ClearingUtils.logoutUser(activity)
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        assert(!sharedPreferences.contains("token"))
    }
}
