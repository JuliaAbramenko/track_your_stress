package com.example.trackyourstress_ba

import com.example.trackyourstress_ba.utility.ClearingUtils
import com.example.trackyourstress_ba.utility.ConnectionUtils
import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.junit.Rule
import org.junit.Test

class SharedPreferencesStartTest {

    @get:Rule
    var activityTestRule: IntentsTestRule<LoginActivity> = IntentsTestRule(
        LoginActivity::class.java
    )

    @Test
    fun areSharedPrefsCleared() {
        val activity = activityTestRule.activity
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        ClearingUtils.logoutUser(activity)
        assert(!sharedPreferences.contains("token"))
        assert(!sharedPreferences.contains("userId"))
        assert(!sharedPreferences.contains("userName"))

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

    @Test
    fun checkIfDailyNotificationEntryExists() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"),
                ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("testtest"), ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click())
        Thread.sleep(2000)
        val sharedPreferences = activityTestRule.activity.getSharedPreferences(
            activityTestRule.activity.packageName, Context.MODE_PRIVATE
        )
        assert(sharedPreferences.contains("dailyNotification"))
    }


    @Test
    fun checkWhetherSharedPreferencesNotNull() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"),
                ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("testtest"), ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click())
        Thread.sleep(2000)
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        val sharedPreferences = activityTestRule.activity.getSharedPreferences(
            activityTestRule.activity.packageName, Context.MODE_PRIVATE
        )
        assert(sharedPreferences != null)
    }
}