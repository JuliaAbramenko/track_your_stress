package com.example.trackyourstress_ba

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.example.trackyourstress_ba.utility.ClearingUtils
import com.example.trackyourstress_ba.utility.ConnectionUtils
import com.example.trackyourstress_ba.ui.home.HomeActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import org.junit.Test

/**
 * General Test Class for SharedPreferences tests
 *
 */
class SharedPreferencesTest {

    /**
     * Verifies whether a token is stored in the SharedPreferences after a login
     *
     */
    @Test
    fun containsToken() {
        val loginActivityTest: IntentsTestRule<LoginActivity> =
            IntentsTestRule(LoginActivity::class.java)
        loginActivityTest.launchActivity(Intent())
        val activity = loginActivityTest.activity
        val conUtils = ConnectionUtils()
        conUtils.loginUser("julia.abramenko@web.de", "testtest", activity)
        Thread.sleep(2000)
        intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        assert(sharedPreferences.contains("token"))
    }

    /**
     * Verifies whether the userName is stored in the SharedPreferences after a login. Needed for
     * the NavigationDrawer menu header
     *
     */
    @Test
    fun containsUserName() {
        val loginActivityTest: IntentsTestRule<LoginActivity> =
            IntentsTestRule(LoginActivity::class.java)
        loginActivityTest.launchActivity(Intent())
        val activity = loginActivityTest.activity
        val conUtils = ConnectionUtils()
        conUtils.loginUser("julia.abramenko@web.de", "testtest", activity)
        Thread.sleep(3000)
        intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        assert(sharedPreferences.contains("userName"))
    }

    /**
     * Verifies that, if a login has been performed with valid credentials and the HomeActivity has
     * been started, the token is deleted from the SharedPreferences on logout execution
     *
     */
    @Test
    fun tokenAfterLogout() {
        val loginActivityTest: IntentsTestRule<LoginActivity> =
            IntentsTestRule(LoginActivity::class.java)
        loginActivityTest.launchActivity(Intent())
        val activity = loginActivityTest.activity
        val conUtils = ConnectionUtils()
        conUtils.loginUser("julia.abramenko@web.de", "testtest", activity)
        Thread.sleep(2000)
        intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        Thread.sleep(2000)
        ClearingUtils.logoutUser(activity)
        val sharedPreferences = activity.getSharedPreferences(
            activity.packageName, Context.MODE_PRIVATE
        )
        assert(!sharedPreferences.contains("token"))
    }
}
