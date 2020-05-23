package com.example.trackyourstress_ba

import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.trackyourstress_ba.ui.start.StartActivity
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test

class LanguageTestsStart {

    @get:Rule
    var mActivityRule: IntentsTestRule<StartActivity> = IntentsTestRule(
        StartActivity::class.java
    )


    @Test
    fun textChangedToGerman() {
        Espresso.onView(ViewMatchers.withId(R.id.germanyFlag)).perform(ViewActions.click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.welcome_text_first_screen)).check(
            ViewAssertions.matches(
                (withText(containsString("willkommen")))
            )
        )
    }

    @Test
    fun changeLocaleToGermanSharedPreferences() {
        Espresso.onView(ViewMatchers.withId(R.id.germanyFlag)).perform(ViewActions.click())
        Thread.sleep(500)
        val activity = mActivityRule.activity
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        val language = sharedPreferences.getString("locale", "en")
        assert(language == "de")

    }

    @Test
    fun textChangedToEnglish() {
        Espresso.onView(ViewMatchers.withId(R.id.britainFlag)).perform(ViewActions.click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.welcome_text_first_screen)).check(
            ViewAssertions.matches(
                (withText(containsString("Welcome")))
            )
        )
    }

    @Test
    fun changeLocaleToEnglishSharedPreferences() {
        Espresso.onView(ViewMatchers.withId(R.id.britainFlag)).perform(ViewActions.click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.welcome_text_first_screen)).check(
            ViewAssertions.matches(
                (withText(containsString("Welcome")))
            )
        )
        val activity = mActivityRule.activity
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        val language = sharedPreferences.getString("locale", "de")
        assert(language == "en")
    }
}