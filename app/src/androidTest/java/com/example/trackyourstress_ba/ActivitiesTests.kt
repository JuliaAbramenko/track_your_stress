package com.example.trackyourstress_ba

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test class for the ActivitiesFragment
 *
 */
class ActivitiesTests {

    /**
     * Testings starts at LoginActivity because a login has to be performed. Only this way, this Fragment
     * can be displayed correctly and has visible functionality
     */
    @get:Rule
    var activityRule: IntentsTestRule<LoginActivity> = IntentsTestRule(
        LoginActivity::class.java
    )

    /**
     * Initialization method to perform the login and select the ActivitiesFragment from the
     * NavigationDrawer
     *
     */
    @Before
    fun init() {
        Espresso.onView(withId(R.id.username))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"),
                ViewActions.pressImeActionButton()
            )
        Espresso.onView(withId(R.id.password))
            .perform(ViewActions.typeText("testtest"), ViewActions.pressImeActionButton())
        Espresso.onView(withId(R.id.login)).perform(click())
        Thread.sleep(2000)
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_activities))
        Thread.sleep(500)
    }

    /**
     * Verifies if the buttons that are only displayed if at least two pages exist (confirmed
     * on 24.05.2020 for the given test account), are clickable and performs navigation forward
     * once and back once
     *
     */
    @Test
    fun pressForwardAndBackClickableTest() {
        Espresso.onView(withText("Continue")).check(matches(isClickable()))
        Espresso.onView(withText("Continue")).perform(click())
        Thread.sleep(500)
        Espresso.onView(withText("Back")).check(matches(isClickable()))
        Espresso.onView(withText("Back")).perform(click())
    }

    /**
     * Verifies if the buttons are displayed. Confirmed on 24.05.2020 for the given test account
     * that those exist
     *
     */
    @Test
    fun checkIfButtonsAreDisplayed() {
        Espresso.onView(withText("Continue")).check(matches(isDisplayed()))
        Espresso.onView(withText("Back")).check(matches(isDisplayed()))
    }
}