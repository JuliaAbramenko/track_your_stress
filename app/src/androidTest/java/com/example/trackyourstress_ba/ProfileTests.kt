package com.example.trackyourstress_ba

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileTests {

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
            .perform(NavigationViewActions.navigateTo(R.id.nav_profile))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.profileFragment)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun retrieveProfileDate() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_firstname))
            .check(matches(withText(containsString("Julia"))))
    }

    @Test
    fun changeProfileContent() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_lastname))
            .perform(clearText(), typeText("TestName"), pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.edit_lastname))
            .check(matches(withText(containsString("TestName"))))
    }

    @Test
    fun checkIfUpdateProfileToastAppears() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_lastname))
            .perform(clearText(), typeText("TestName"), pressImeActionButton(), closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.submit_button_update_profile))
            .perform(ViewActions.click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withText(R.string.profile_updated)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(mActivityRule.activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun testIfChangePasswordDialogPopsUp() {
        Espresso.onView(ViewMatchers.withId(R.id.button_change_password)).perform(click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withText("Old password")).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(mActivityRule.activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

}