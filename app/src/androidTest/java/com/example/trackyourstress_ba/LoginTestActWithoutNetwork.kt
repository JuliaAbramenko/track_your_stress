package com.example.trackyourstress_ba

import android.Manifest
import android.content.Context
import android.net.wifi.WifiManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test class for the LoginActivity that has no internet connection
 *
 * This test class works on all API versions lesser than 29. Currently there is no replacement for the
 * functionality that was deprecated.
 * See https://developer.android.com/reference/android/net/wifi/WifiManager.html#setWifiEnabled(boolean)
 * for further information. Delete comments when testing on devices with API version lesser than 29.
 * Or execute this class manually with disabled wifi.
 *
 *
 */

/*
@Suppress("DEPRECATION")
class LoginTestActWithoutNetwork {

    /**
     * The activity to be tested: LoginActivity
     */
    @get:Rule
    var activityTestRule: ActivityTestRule<LoginActivity> =
        ActivityTestRule(LoginActivity::class.java)

    /**
     * Grants a permission rule that allows to change the state of the device WiFi
     */
    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE
    )

    /**
     * Disables the WiFi before text execution
     *
     */
    @Before
    fun init() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false
    }

    /**
     * Actual test. Given valid credentials, it is verified whether the Toast informing the user
     * about missing network connection, appears when trying to login
     *
     */
    @Test
    fun testWithoutNetwork() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(
                ViewActions.typeText("julia.abramenko@web.de"),
                ViewActions.pressImeActionButton()
            )
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("testtest"), ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click())
        Thread.sleep(3000)
        val activity = activityTestRule.activity
        Espresso.onView(ViewMatchers.withText(R.string.network_error)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(activity.window.decorView)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    /**
     * Finishing method of the class. Sets the WiFi enabling to true and provides internet connection
     * for further tests
     *
     */
    @After
    fun end() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = true
    }
}
*/
