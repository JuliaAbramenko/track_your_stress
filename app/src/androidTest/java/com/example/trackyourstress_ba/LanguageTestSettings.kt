package com.example.trackyourstress_ba

import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LanguageTestSettings {

    @get:Rule
    var mActivityRule: IntentsTestRule<LoginActivity> = IntentsTestRule(
        LoginActivity::class.java
    )

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
        Thread.sleep(3000)
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_notifications))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.settingsBase)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun checkFlagsExistInSettingsFragment() {
        Espresso.onView(ViewMatchers.withId(R.id.germanFlag))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.englishFlag))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun textIsUpdatedWhenEnglishFlagClicked() {
        Espresso.onView(ViewMatchers.withId(R.id.englishFlag)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.manage_notifications_textview)).check(
            ViewAssertions.matches(
                (ViewMatchers.withText(CoreMatchers.containsString("notifications")))
            )
        )
    }

    @Test
    fun textIsUpdatedWhenGermanFlagClicked() {
        Espresso.onView(ViewMatchers.withId(R.id.germanFlag)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.manage_notifications_textview)).check(
            ViewAssertions.matches(
                (ViewMatchers.withText(CoreMatchers.containsString("Benachrichtigungen")))
            )
        )
    }

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


}