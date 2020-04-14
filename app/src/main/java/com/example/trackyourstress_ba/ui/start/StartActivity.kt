package com.example.trackyourstress_ba.ui.start

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.register.RegistrationActivity
import java.util.*


class StartActivity : AppCompatActivity() {
    lateinit var loginButton: Button
    lateinit var registerButton: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var germanFlag: ImageView
    lateinit var englishFlag: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
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

    private fun setGermanLocale() {
        val config = Configuration(resources.configuration)
        config.locale = Locale.GERMAN
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun setEnglishLocale() {
        val config = Configuration(resources.configuration)
        config.locale = Locale.ENGLISH
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun updateText() {
        val textView = findViewById<TextView>(R.id.welcome_text_first_screen)
        textView.text = getString(R.string.welcome_text)
        registerButton.text = getString(R.string.Registration)
    }
}

