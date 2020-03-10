package com.example.trackyourstress_ba

import android.app.PendingIntent.getActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import java.util.regex.Pattern.matches


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
