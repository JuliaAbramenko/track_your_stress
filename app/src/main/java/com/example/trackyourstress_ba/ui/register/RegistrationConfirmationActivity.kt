package com.example.trackyourstress_ba.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.ui.start.StartActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.ui.login.LoginActivity

/**
 * The activity coming after the RegistrationActivity. To get here, the submit button in the
 * RegistrationActivity has to be used and a registration try performed.
 */
class RegistrationConfirmationActivity : AppCompatActivity() {
    private lateinit var registrationLinkSent: TextView
    private lateinit var backButton: Button
    private lateinit var resendVerificationButton: Button
    private lateinit var conUtils: ConnectionUtils

    /**
     * general creation method for the RegisterActivity. Identification of relevant fields and buttons
     * are made.
     * Adds click listeners to the buttons and an option to resend a verification link.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = getString(R.string.Registration)
        setContentView(R.layout.activity_registration__confirmation)
        registrationLinkSent = findViewById(R.id.registration_confirm_text)
        backButton = findViewById(R.id.register_back)
        resendVerificationButton = findViewById(R.id.resend_verfication_link_button)
        conUtils = ConnectionUtils()

        resendVerificationButton.setOnClickListener {
            val preferences = this.getSharedPreferences(
                this.packageName, Context.MODE_PRIVATE
            )
            val email = preferences.getString("userEmail", null)
            if (email != null) {
                conUtils.resendVerificationLink(email, this)
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this@RegistrationConfirmationActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Displayed toast when a verification link has been resent
     *
     */
    fun linkResent() {
        Toast.makeText(
            applicationContext,
            getString(R.string.link_sent_check_email),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Displayed toast with error when user was not found trying to register.
     *
     */
    fun notify400() {
        Toast.makeText(
            applicationContext,
            getString(R.string.link_send_not_possible),
            Toast.LENGTH_LONG
        ).show()
    }
}
