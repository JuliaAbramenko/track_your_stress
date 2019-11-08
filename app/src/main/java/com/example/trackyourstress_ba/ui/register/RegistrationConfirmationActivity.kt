package com.example.trackyourstress_ba.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.trackyourstress_ba.R

class RegistrationConfirmationActivity : AppCompatActivity() {

    lateinit var registration_link_send: TextView
    lateinit var back_button: Button
    lateinit var resend_verification_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration__confirmation)

        registration_link_send = findViewById(R.id.registration_confirm_text)
        back_button = findViewById(R.id.register_back)
        resend_verification_button = findViewById(R.id.resend_verfication_link_button)
    }
}
