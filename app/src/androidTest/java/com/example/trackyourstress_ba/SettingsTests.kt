package com.example.trackyourstress_ba

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test class for the SettingsFragment
 *
 */
class SettingsTests {

    /**
     * Definition of the Activity to be tested: LoginActivity
     */
    @get:Rule
    var mActivityRule: IntentsTestRule<LoginActivity> = IntentsTestRule(
        LoginActivity::class.java
    )

    /**
     * Login performance and selection of the SettingsFragment in the NavigationDrawer before
     * every test execution
     *
     */
    @Before
    fun init() {
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
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_notifications))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.settingsBase)).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    /**
     * Tests whether the two flags for language (german and english)
     * are displayed in the SettingsFragment
     *
     */
    @Test
    fun checkFlagsExistInSettingsFragment() {
        Espresso.onView(ViewMatchers.withId(R.id.germanFlag))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.englishFlag))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Verifies whether language changes are applied on the TextView "manage_notification_textview"
     * when the english flag is selected
     *
     */
    @Test
    fun textIsUpdatedWhenEnglishFlagClicked() {
        Espresso.onView(ViewMatchers.withId(R.id.englishFlag)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.manage_notifications_textview)).check(
            matches(
                (ViewMatchers.withText(CoreMatchers.containsString("notifications")))
            )
        )
    }

    /**
     * Verifies whether language changes are applied on the TextView "manage_notification_textview"
     * when the german flag is selected
     */
    @Test
    fun textIsUpdatedWhenGermanFlagClicked() {
        Espresso.onView(ViewMatchers.withId(R.id.germanFlag)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.manage_notifications_textview)).check(
            matches(
                (ViewMatchers.withText(CoreMatchers.containsString("Benachrichtigungen")))
            )
        )
    }

    /**
     * Verifies whether the SharedPreferences entries "dailyNotification", "weeklyNotification" and
     * "monthlyNotification" are updated when the switch for all notification
     * types is deactivated
     *
     */
    @Test
    fun checkIfSharedPreferencesAreUpdatedAfterSwitchChange() {
        Espresso.onView(ViewMatchers.withId(R.id.switch_all_notifications))
            .perform(ViewActions.click())
        val activity = mActivityRule.activity
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        assert(!sharedPreferences.getBoolean("dailyNotification", true))
        assert(!sharedPreferences.getBoolean("weeklyNotification", true))
        assert(!sharedPreferences.getBoolean("monthlyNotification", true))
        Espresso.onView(ViewMatchers.withId(R.id.switch_all_notifications))
            .perform(ViewActions.click())
    }

    /**
     * Extracts the SharedPreferences entries for notification types and verifies whether the switch
     * states are the same as the values stored in the SharedPreferences
     *
     */
    @Test
    fun checkWhetherNotificationTypesAreCorrect() {
        val sharedPreferences = mActivityRule.activity.getSharedPreferences(
            mActivityRule.activity.packageName, Context.MODE_PRIVATE
        )
        val daily = sharedPreferences.getBoolean("dailyNotification", false)
        val weekly = sharedPreferences.getBoolean("weeklyNotification", false)
        val monthly = sharedPreferences.getBoolean("monthlyNotification", false)
        if (daily) {
            Espresso.onView(ViewMatchers.withId(R.id.switch_daily_notfications))
                .check(matches(isChecked()))
        } else Espresso.onView(ViewMatchers.withId(R.id.switch_daily_notfications)).check(
            matches(
                not(
                    isChecked()
                )
            )
        )
        if (weekly) {
            Espresso.onView(ViewMatchers.withId(R.id.switch_weekly_notifications))
                .check(matches(isChecked()))
        } else Espresso.onView(ViewMatchers.withId(R.id.switch_weekly_notifications)).check(
            matches(
                not(
                    isChecked()
                )
            )
        )
        if (monthly) {
            Espresso.onView(ViewMatchers.withId(R.id.switch_monthly_notifications))
                .check(matches(isChecked()))
        } else Espresso.onView(ViewMatchers.withId(R.id.switch_monthly_notifications)).check(
            matches(
                not(
                    isChecked()
                )
            )
        )

    }

    /**
     * Small check to verify if the toolbar title is correctly set when the SettingsFragment
     * is selected
     *
     */
    @Test
    fun toolbarCorrect() {
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "Settings")
    }


}