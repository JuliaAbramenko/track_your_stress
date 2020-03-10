package com.example.trackyourstress_ba

import android.Manifest
import android.content.Context
import android.net.wifi.WifiManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginTestActWithNetwork {
    @get:Rule
    var activityTestRule: ActivityTestRule<LoginActivity> =
        ActivityTestRule(LoginActivity::class.java)

    /**
     * test 1 and 2 require Internet Access to run properly
     */
    @Test
    fun testFailedLogin401() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"),
                ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("tniojiklds"), ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click())
        Thread.sleep(3000)
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withText(R.string.notify401)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun test2FailedLogin401() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(
                ViewActions.typeText("julia.abramenko@uni-ulm.de"),
                ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("testtest"), ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click())
        Thread.sleep(3000)
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withText(R.string.notify401)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun testPasswordTooShort() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"),
                ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("test"), ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click())
        Thread.sleep(3000)
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withText(R.string.password_short)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

}