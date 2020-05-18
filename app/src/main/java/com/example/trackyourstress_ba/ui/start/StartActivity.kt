package com.example.trackyourstress_ba.ui.start

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.registration.RegistrationActivity
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The first Activity to be displayed. Contains options for login and registration.
 *
 */
@Suppress("DEPRECATION")
class StartActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var germanFlag: ImageView
    private lateinit var englishFlag: ImageView
    private lateinit var logo: ImageView


    /**
     *  general creation method for the StartActivity.
     *  Function to identify relevant Buttons and ImageViews to initiate click listeners.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        logo = findViewById(R.id.Welcome_Logo)
        val smaller5Inches = checkIfSmaller5Inches()
        if (smaller5Inches) {
            logo.layoutParams.width = 150
            logo.layoutParams.height = 150
        }

        loginButton = findViewById(R.id.login_button)
        registerButton = findViewById(R.id.register_button)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finishActivity(0)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finishActivity(0)
        }

        sharedPreferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        germanFlag = findViewById(R.id.germanyFlag)
        germanFlag.setOnClickListener {
            sharedPreferences.edit().putString("locale", "de").apply()
            setGermanLocale()
            updateText()
        }
        englishFlag = findViewById(R.id.britainFlag)
        englishFlag.setOnClickListener {
            sharedPreferences.edit().putString("locale", "en").apply()
            setEnglishLocale()
            updateText()
        }
    }


    /**
     * Calculates the screen diagonal by retrieving the screen pixel size
     *
     * @return a Boolean whether the display has a diagonal of less than 5 inches
     */
    private fun checkIfSmaller5Inches(): Boolean {
        val dm = DisplayMetrics()
        this.windowManager!!.defaultDisplay.getMetrics(dm)
        val x = (dm.widthPixels / dm.xdpi).toDouble().pow(2.0)
        val y = (dm.heightPixels / dm.ydpi).toDouble().pow(2.0)
        val screenInches = sqrt(x + y)
        Log.d("Inch calculation:", screenInches.toString())
        return screenInches < 5
    }

    /**
     * Change the app language to German.
     *
     */
    private fun setGermanLocale() {
        val config = Configuration(resources.configuration)
        config.locale = Locale.GERMAN
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * Change the app language to English
     *
     */
    private fun setEnglishLocale() {
        val config = Configuration(resources.configuration)
        config.locale = Locale.ENGLISH
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * Function that updates all visible Views so that language changes are seen immediately.
     *
     */
    private fun updateText() {
        val textView = findViewById<TextView>(R.id.welcome_text_first_screen)
        textView.text = getString(R.string.welcome_text)
        registerButton.text = getString(R.string.Registration)
    }
}

