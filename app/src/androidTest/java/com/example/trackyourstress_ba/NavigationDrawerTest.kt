package com.example.trackyourstress_ba

import android.view.Gravity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.trackyourstress_ba.ui.home.HomeActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

/**
 * Class for NavigationDrawer click tests
 *
 */
class NavigationDrawerTest {

    /**
     * The activity that shall be tested: HomeActivity
     */
    @get:Rule
    var rule: IntentsTestRule<HomeActivity> = IntentsTestRule(HomeActivity::class.java)

    /**
     * Checks whether the NavigationDrawer appears on the left hand side of the screen
     *
     */
    @Test
    fun navigationDrawerAppears() {
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.START)))
            .perform(DrawerActions.open())
    }

    /**
     * Verifies that the NavigationDrawer closes when the back button of the device has been
     * clicked
     *
     */
    @Test
    fun pressingBackClosesNavigationDrawer() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.nav_view)).check(matches(not(isDisplayed())))
    }

    /**
     * Verifies if the NavigationDrawer is displayed at all when the button in the left top corner
     * is clicked
     *
     */
    @Test
    fun navigationDrawerOpens() {
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.START))).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view)).check(matches(isDisplayed()))
    }

    /**
     * Verifies whether the basic layout of the ImprintFragment is displayed when the Fragment
     * is selected in the NavigationDrawer
     *
     */
    @Test
    fun imprintFragmentExists() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_impressum))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.impressum_root)).check(matches(isDisplayed()))
    }

    /**
     * Verifies whether the basic layout of the ProfileFragment is displayed when the Fragment
     * is selected in the NavigationDrawer
     *
     */
    @Test
    fun profileFragmentExists() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_profile))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.profileFragment)).check(matches(isDisplayed()))
    }

    /**
     * Verifies whether the basic layout of the QuestionnairesFragment is displayed when the Fragment
     * is selected in the NavigationDrawer
     *
     */
    @Test
    fun questionnairesFragmentExists() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_questionnaires))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.questionnaire_table)).check(matches(isDisplayed()))
    }

    /**
     * Verifies whether the basic layout of the ActivitiesFragment is displayed when the Fragment
     * is selected in the NavigationDrawer
     *
     */
    @Test
    fun activitiesFragmentExists() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_activities))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.activity_scroll)).check(matches(isDisplayed()))
    }

    /**
     * Verifies whether the basic layout of the HomeFragment is displayed when the Fragment
     * is selected in the NavigationDrawer
     *
     */
    @Test
    fun homeFragmentExists() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_home))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.logoUlm)).check(matches(isDisplayed()))
    }

    /**
     * Verifies whether the basic layout of the SettingsFragment is displayed when the Fragment
     * is selected in the NavigationDrawer
     *
     */
    @Test
    fun settingsFragmentExists() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_notifications))
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.settingsBase)).check(matches(isDisplayed()))
    }
}