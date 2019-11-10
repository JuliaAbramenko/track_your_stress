package com.example.trackyourstress_ba.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.kotlin.GlobalVariables

class RegistrationConfirmationActivity : AppCompatActivity() {

    lateinit var registration_link_send: TextView
    lateinit var back_button: Button
    lateinit var resend_verification_button: Button
    lateinit var con_utils : ConnectionUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration__confirmation)

        con_utils = ConnectionUtils()

        registration_link_send = findViewById(R.id.registration_confirm_text)
        back_button = findViewById(R.id.register_back)
        resend_verification_button = findViewById(R.id.resend_verfication_link_button)

        resend_verification_button.setOnClickListener {
            val email = GlobalVariables.localStorage["current_email"].toString()
            con_utils.resendVerificationLink(email, this)
        }

        back_button.setOnClickListener {
            val intent = Intent(this@RegistrationConfirmationActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
