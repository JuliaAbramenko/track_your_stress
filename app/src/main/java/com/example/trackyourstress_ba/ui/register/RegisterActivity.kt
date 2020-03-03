package com.example.trackyourstress_ba.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils

class RegisterActivity : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editPasswordConfirmation: EditText
    private lateinit var editUsername: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var registerButton: Button
    private lateinit var conUtils: ConnectionUtils
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editEmail = findViewById(R.id.register_email_field)
        editPassword = findViewById(R.id.register_password_field)
        editPasswordConfirmation = findViewById(R.id.register_password_confirmation_field)
        editUsername = findViewById(R.id.register_username_field)
        checkBox = findViewById(R.id.save_data_confirm)
        registerButton = findViewById(R.id.register_ok_button)
        conUtils = ConnectionUtils()
        backButton = findViewById(R.id.tohome_button_reg)

        registerButton.setOnClickListener {
            if (!editEmail.text.contains("@") && !editEmail.text.contains(".")) {
                emailNotValid()
            }
            if (editPassword.text.length <= 7) {
                passwordTooShort()
            }
            if (editPassword.text != editPasswordConfirmation.text) {
                passwordsNotMatching()
            }
            if (!checkBox.isChecked) {
                needCheck()
            }
            if (editEmail.text.contains("@") && editEmail.text.contains(".") &&
                editPassword.text.length > 7 && editPassword.text == editPasswordConfirmation.text &&
                editPasswordConfirmation.text.length > 7 &&
                editUsername.text.length > 7
            ) {
                conUtils.registerUser(
                    editEmail.text.toString(), editPassword.text.toString(),
                    editPasswordConfirmation.text.toString(), editUsername.text.toString(), this
                )
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun passwordsNotMatching() {
        Toast.makeText(
            applicationContext,
            "Die Passwörter stimmen nicht überein",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun passwordTooShort() {
        Toast.makeText(
            applicationContext,
            "Das Passwort ist zu kurz",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun emailNotValid() {
        Toast.makeText(
            applicationContext,
            "Die Email ist nicht gültig. Prüfen Sie Ihre Eingabe",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun needCheck() {
        Toast.makeText(
            applicationContext,
            "Bitte bestätigen Sie die Bedingung",
            Toast.LENGTH_LONG
        ).show()
    }

    fun registrationSuccessful() {
        val intent = Intent(this@RegisterActivity, RegistrationConfirmationActivity::class.java)
        startActivity(intent)
    }

    fun registrationError() {
        Toast.makeText(
            applicationContext,
            "Fehler bei der Registrierung. Bitte versuchen Sie es später erneut",
            Toast.LENGTH_LONG
        ).show()
    }
}
