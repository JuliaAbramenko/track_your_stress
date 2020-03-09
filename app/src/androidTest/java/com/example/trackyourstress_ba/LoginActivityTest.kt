package com.example.trackyourstress_ba

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.junit.Rule
import org.junit.Test


class LoginActivityTest {

    @get:Rule
    var mActivityRule: IntentsTestRule<LoginActivity> = IntentsTestRule(LoginActivity::class.java)

    @Test
    fun testLogin() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(typeText("julia.abramenko@web.de"), pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(typeText("testtest"), pressImeActionButton())
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
    }

    fun testFailLogin() {

    }

}