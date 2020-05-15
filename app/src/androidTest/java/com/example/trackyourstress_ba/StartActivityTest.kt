package com.example.trackyourstress_ba

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.registration.RegistrationActivity
import com.example.trackyourstress_ba.ui.start.StartActivity
import org.junit.Rule
import org.junit.Test


class StartActivityTest {

    @get:Rule
    var mActivityRule: IntentsTestRule<StartActivity> = IntentsTestRule(
        StartActivity::class.java
    )

    @Test
    fun testLoginButton() {
        onView(withId(R.id.login_button)).perform(click())
        intended(hasComponent(LoginActivity::class.java.name))
    }

    @Test
    fun testRegisterButton() {
        onView(withId(R.id.register_button)).perform(click())
        intended(hasComponent(RegistrationActivity::class.java.name))
    }
}