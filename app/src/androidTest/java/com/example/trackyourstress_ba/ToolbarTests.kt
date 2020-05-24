package com.example.trackyourstress_ba

import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test class for all Toolbar titles inside the HomeActivity
 *
 */
class ToolbarTests {

    /**
     * Definition of the rule for the activity to be tested: the LoginActivity. This is a requirement
     * as, without login, the fragments do not function properly.
     */
    @get:Rule
    var mActivityRule: IntentsTestRule<LoginActivity> = IntentsTestRule(
        LoginActivity::class.java
    )

    /**
     * Login performed at the beginning of each test
     *
     */
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
        Thread.sleep(2000)
    }


    /**
     * Verifies whether the title of the HomeFragment is set correctly after checking that the base
     * view of the Fragment is displayed
     *
     */
    @Test
    fun homeTitleCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_home))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.homeText)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "Home")
    }

    /**
     * Verifies whether the title of the ProfileFragment is set correctly after checking that the base
     * view of the Fragment is displayed
     *
     */
    @Test
    fun profileTitleCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_profile))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.profileFragment)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "Profile")
    }

    /**
     * Verifies whether the title of the ActivitiesFragment is set correctly after checking that the base
     * view of the Fragment is displayed
     *
     */
    @Test
    fun activitiesTitleCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_activities))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.activitiesRootLayout)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "Activities")
    }

    /**
     * Verifies whether the title of the QuestionnairesFragment is set correctly after checking that the base
     * view of the Fragment is displayed
     *
     */
    @Test
    fun questionnairesTitleCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_questionnaires))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.questionnaire_table)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "Questionnaires")
    }

    /**
     * Verifies whether the title of the SettingsFragment is set correctly after checking that the base
     * view of the Fragment is displayed
     *
     */
    @Test
    fun settingsTitleCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_notifications))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.settingsBase)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "Settings")
    }

    /**
     * Verifies whether the title of the ImprintFragment is set correctly
     *
     */
    @Test
    fun imprintTitleCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_impressum))
        Thread.sleep(500)
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "Imprint")
    }


    /**
     * Verifies whether the title of the Fragment for 'About us' is set correctly
     *
     */
    @Test
    fun aboutUsTitleCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_about_us))
        Thread.sleep(500)
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "About us")
    }

    /**
     * Check whether the title is set to "TrackYourStress" again, when a logout is executed
     *
     */
    @Test
    fun logoutTitleCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_logout))
        Thread.sleep(500)
        val activity = mActivityRule.activity
        val activityCompat = activity as AppCompatActivity
        assert(activityCompat.supportActionBar?.title == "TrackYourStress")
    }
}