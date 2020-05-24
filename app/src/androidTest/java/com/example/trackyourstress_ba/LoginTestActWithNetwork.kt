package com.example.trackyourstress_ba

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test

/**
 * Test class for the LoginActivity with network connection. Can only be run if network connection
 * is provided
 *
 */
class LoginTestActWithNetwork {
    @get:Rule
    var activityTestRule: ActivityTestRule<LoginActivity> =
        ActivityTestRule(LoginActivity::class.java)

    /**
     * Tests whether the Toast telling the user that the credentials are invalid is displayed.
     * Tries a login request with invalid credentials and gets an 401 HTTP error.
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

    /**
     * Test for verifying that an email that is not registered at the platform cannot be used to
     * login. Tries a login request with invalid credentials and gets an 401 HTTP error.
     *
     */
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

    /**
     * Test that does not require API requests. Only verifies that a password has a length of
     * at least 8 characters to be able to try to perform a login request
     *
     */
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