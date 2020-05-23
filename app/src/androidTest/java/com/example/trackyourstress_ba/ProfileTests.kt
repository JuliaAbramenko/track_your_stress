package com.example.trackyourstress_ba

import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
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
        Espresso.onView(withId(R.id.username))
            .perform(
                typeText("julia.abramenko@web.de"),
                pressImeActionButton()
            )
        Espresso.onView(withId(R.id.password))
            .perform(typeText("testtest"), pressImeActionButton())
        Espresso.onView(withId(R.id.login)).perform(click())
        Thread.sleep(2000)
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_profile))
        Thread.sleep(500)
        Espresso.onView(withId(R.id.profileFragment)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun isProfileDataReceived() {
        Espresso.onView(withId(R.id.edit_firstname))
            .check(matches(withText(containsString("Julia"))))
    }

    @Test
    fun changeProfileContent() {
        Espresso.onView(withId(R.id.edit_lastname))
            .perform(clearText(), typeText("TestName"), pressImeActionButton())
        Espresso.onView(withId(R.id.edit_lastname))
            .check(matches(withText(containsString("TestName"))))
    }

    @Test
    fun checkIfUpdateProfileToastAppears() {
        Espresso.onView(withId(R.id.edit_lastname))
            .perform(clearText(), typeText("TestName"), pressImeActionButton(), closeSoftKeyboard())
        Espresso.onView(withId(R.id.submit_button_update_profile))
            .perform(click())
        Thread.sleep(500)
        Espresso.onView(withText(R.string.profile_updated)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(mActivityRule.activity.window.decorView)
            )
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun testIfChangePasswordDialogPopsUp() {
        Espresso.onView(withId(R.id.button_change_password)).perform(click())
        Thread.sleep(500)
        Espresso.onView(withText("Old password")).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(mActivityRule.activity.window.decorView)
            )
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun closeDialog() {
        Espresso.onView(withId(R.id.button_change_password)).perform(click())
        Thread.sleep(500)
        Espresso.onView(withText("Old password")).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(mActivityRule.activity.window.decorView)
            )
        ).check(
            matches(isDisplayed())
        )
        Espresso.onView(withText("Cancel")).perform(click())
    }

    @Test
    fun closesDialog() {
        Espresso.onView(withId(R.id.button_change_password)).perform(click())
        Thread.sleep(500)
        Espresso.onView(withText("Cancel")).inRoot(isDialog()).check(matches(isDisplayed()))
            .perform(click())

    }

    @Test
    fun toolbarTitleCorrect() {
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "Profile")
    }


}