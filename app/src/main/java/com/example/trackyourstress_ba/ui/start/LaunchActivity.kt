package com.example.trackyourstress_ba.ui.start

import android.content.Intent
import com.example.trackyourstress_ba.ui.home.HomeActivity
import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.*


@Suppress("DEPRECATION")
/**
 * Inivisible launcher activity. Used for navigation reasons depending on whether a token exists
 * whenever the app is started.
 *
 */
class LaunchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        val locale = sharedPreferences.getString("locale", "de")!!
        checkLocale(locale)
        if (sharedPreferences.contains("token") && sharedPreferences.getString(
                "token",
                ""
            ) != ""
        ) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Adapt the language on SharedPreferences "locale" value when the app has at least started once.
     *
     * @param locale language abbreviation from SharedPreferences.
     */
    private fun checkLocale(locale: String) {
        val config = Configuration(resources.configuration)
        if (locale == "de") {
            config.locale = Locale.GERMAN
            resources.updateConfiguration(config, resources.displayMetrics)
        } else {
            config.locale = Locale.ENGLISH
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
}