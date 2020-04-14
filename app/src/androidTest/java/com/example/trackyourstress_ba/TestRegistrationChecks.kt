package com.example.trackyourstress_ba

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.example.trackyourstress_ba.ui.register.RegistrationActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test

class TestRegistrationChecks {
    @get:Rule
    var activityTestRule: ActivityTestRule<RegistrationActivity> =
        ActivityTestRule(RegistrationActivity::class.java)

    @Test
    fun checkDomain() {
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withId(R.id.register_email_field))
            .perform(
                ViewActions.typeText("julia.abramenko"), ViewActions.closeSoftKeyboard()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_ok_button)).perform(click())
        Espresso.onView(ViewMatchers.withText(R.string.email_invalid)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun checkPasswordTooShort() {
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withId(R.id.register_email_field))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"), ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_password_field))
            .perform(
                ViewActions.typeText("test"), ViewActions.closeSoftKeyboard()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_ok_button)).perform(click())
        Espresso.onView(ViewMatchers.withText(R.string.password_too_short)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun checkPasswordMatching() {
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withId(R.id.register_email_field))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"), ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_password_field))
            .perform(
                ViewActions.typeText("testtest"), ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_password_confirmation_field))
            .perform(
                ViewActions.typeText("testte"), ViewActions.closeSoftKeyboard()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_ok_button)).perform(click())
        Espresso.onView(ViewMatchers.withText(R.string.password_not_match)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun ifCheckBoxChecked() {
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withId(R.id.register_email_field))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"), ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_password_field))
            .perform(
                ViewActions.typeText("testtest"), ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_password_confirmation_field))
            .perform(
                ViewActions.typeText("testtest"), ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_username_field))
            .perform(
                ViewActions.typeText("testaccount200"), ViewActions.closeSoftKeyboard()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_ok_button)).perform(click())
        Espresso.onView(ViewMatchers.withText(R.string.confirm_requirement)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun checkUsername() {
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withId(R.id.register_email_field))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"), ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_password_field))
            .perform(
                ViewActions.typeText("testtest"), ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.register_password_confirmation_field))
            .perform(
                ViewActions.typeText("testtest"), ViewActions.closeSoftKeyboard()
            )
        Espresso.onView(ViewMatchers.withId(R.id.save_data_confirm)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.register_ok_button)).perform(click())
        Espresso.onView(ViewMatchers.withText(R.string.username_needed)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }
}