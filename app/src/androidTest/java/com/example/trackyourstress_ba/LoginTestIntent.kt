package com.example.trackyourstress_ba

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.junit.Rule
import org.junit.Test


class LoginTestIntent {

    @get:Rule
    var intentsTestRule: IntentsTestRule<LoginActivity> = IntentsTestRule(LoginActivity::class.java)

    @Test
    fun testSuccessfulLogin() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(typeText("julia.abramenko@web.de"), pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(typeText("testtest"), pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(click())
        Thread.sleep(2000)
        intended(hasComponent(HomeActivity::class.java.name))
    }

}
