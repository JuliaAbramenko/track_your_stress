package com.example.trackyourstress_ba

import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.registration.RegistrationActivity
import com.example.trackyourstress_ba.ui.start.StartActivity
import org.junit.Rule
import org.junit.Test

/**
 * General test class for the StartActivity
 *
 */
class StartActivityTest {

    /**
     * Defines the activity to be tested: StartActivity
     */
    @get:Rule
    var mActivityRule: IntentsTestRule<StartActivity> = IntentsTestRule(
        StartActivity::class.java
    )

    /**
     * Verifies whether the login button is added to the visible view
     *
     */
    @Test
    fun testLoginButton() {
        onView(withId(R.id.login_button)).perform(click())
        intended(hasComponent(LoginActivity::class.java.name))
    }

    /**
     * Verifies whether the register button is added to the visible view
     *
     */
    @Test
    fun testRegisterButton() {
        onView(withId(R.id.register_button)).perform(click())
        intended(hasComponent(RegistrationActivity::class.java.name))
    }

    /**
     * Verifies whether the logo is added to the visible view
     *
     */
    @Test
    fun logoVisible() {
        onView(withId(R.id.Welcome_Logo)).check(matches(isDisplayed()))
    }

    /**
     * Checks whether both flags are added to the view
     *
     */
    @Test
    fun flagsVisible() {
        onView(withId(R.id.germanyFlag)).check(matches(isDisplayed()))
        onView(withId(R.id.britainFlag)).check(matches(isDisplayed()))
    }

    /**
     * Test for the toolbar title in the StartActivity. It should be TrackYourStress as no Fragment
     * can be selected in this state
     *
     */
    @Test
    fun toolbarTitleCorrect() {
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "TrackYourStress")
    }
}