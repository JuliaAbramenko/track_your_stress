package com.example.trackyourstress_ba.ui.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.ui.start.StartActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils

class RegistrationActivity : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editPasswordConfirmation: EditText
    private lateinit var editUsername: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var registerButton: Button
    private lateinit var conUtils: ConnectionUtils
    private lateinit var backButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = getString(R.string.Registration)
        setContentView(R.layout.activity_register)


        editEmail = findViewById(R.id.register_email_field)
        editPassword = findViewById(R.id.register_password_field)
        editPasswordConfirmation = findViewById(R.id.register_password_confirmation_field)
        editUsername = findViewById(R.id.register_username_field)
        checkBox = findViewById(R.id.save_data_confirm)
        registerButton = findViewById(R.id.register_ok_button)
        conUtils = ConnectionUtils()
        backButton = findViewById(R.id.tohome_button_reg)

        sharedPreferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )

        if (!sharedPreferences.contains("apiEndpoint")) {
            sharedPreferences.edit().putString("apiEndpoint", "https://api.trackyourstress.org")
                .apply()
        }
        if (!sharedPreferences.contains("currentLanguage")) {
            sharedPreferences.edit().putString("currentLanguage", "de").apply() //default germanFlag
        }

        registerButton.setOnClickListener {
            if (!editEmail.text.contains("@") || !editEmail.text.contains(".")) {
                emailNotValid()
            } else if (editPassword.text.length <= 7) {
                passwordTooShort()
            } else if (editPassword.text.toString() != editPasswordConfirmation.text.toString()) {
                passwordsNotMatching()
            } else if (!checkBox.isChecked) {
                needCheck()
            } else if (editUsername.text.isEmpty()) {
                userNameNeeded()
            } else {
                val preferences = this.getSharedPreferences(
                    this.packageName, Context.MODE_PRIVATE
                )
                preferences.edit().putString("userEmail", editEmail.text.toString()).apply()
                conUtils.registerUser(
                    editEmail.text.toString(), editPassword.text.toString(),
                    editPasswordConfirmation.text.toString(), editUsername.text.toString(), this
                )
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, StartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userNameNeeded() {
        Toast.makeText(
            applicationContext,
            getString(R.string.username_needed),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun passwordsNotMatching() {
        Toast.makeText(
            applicationContext,
            getString(R.string.password_not_match),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun passwordTooShort() {
        Toast.makeText(
            applicationContext,
            getString(R.string.password_too_short),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun emailNotValid() {
        Toast.makeText(
            applicationContext,
            getString(R.string.email_invalid),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun needCheck() {
        Toast.makeText(
            applicationContext,
            getString(R.string.confirm_requirement),
            Toast.LENGTH_LONG
        ).show()
    }

    fun nextStep() {
        val intent = Intent(this@RegistrationActivity, RegistrationConfirmationActivity::class.java)
        startActivity(intent)
    }


    fun abort() {
        Toast.makeText(
            applicationContext,
            "Der Nutzername is bereits vergeben.",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    fun notifyError() {
        Toast.makeText(
            applicationContext,
            "Ein Fehler ist aufgetreten",
            Toast.LENGTH_LONG
        ).show()
    }

    fun noNetworkConnection() {
        Toast.makeText(
            applicationContext,
            "Kein Internet",
            Toast.LENGTH_LONG
        ).show()
    }
}
