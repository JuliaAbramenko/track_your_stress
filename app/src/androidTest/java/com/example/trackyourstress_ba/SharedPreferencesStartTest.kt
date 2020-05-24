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

/**
 * Test class for SharedPreferences in the first few activities
 *
 */
class SharedPreferencesStartTest {

    /**
     * Defines the activity to be tested: LoginActivity
     */
    @get:Rule
    var activityTestRule: IntentsTestRule<LoginActivity> = IntentsTestRule(
        LoginActivity::class.java
    )

    /**
     * Performs an API request to the server without GUI interaction and verifies whether the
     * items "token", "userId" and "userName" are deleted when executing a logout request
     *
     */
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

    /**
     * Performs a login with valid credentials (verified 24.05.2020) without GUI interaction.
     * Checks whether a token is stored in the SharedPreferences when the login went successful
     *
     */
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

    /**
     * Verifies that after an login with valid credentials (date: 24.05.2020), the key "dailyNotification"
     * has been stored, implying that the HomeActivity has been launched correctly
     *
     */
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


    /**
     * Verifies that SharedPreferences exist, when a login has been performed successfully and
     * the HomeActivity is started
     *
     */
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