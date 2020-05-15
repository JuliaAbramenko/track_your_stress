package com.example.trackyourstress_ba.ui.registration

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
import com.example.trackyourstress_ba.utility.ConnectionUtils

/**
 * The activity that handles the registration. To get here, the register button in the StartActivity has
 * to be used.
 */
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

    /**
     * general creation method for the RegisterActivity. Identification of relevant fields and buttons
     * are made as well relevant values "apiEndpoint" and "locale" are stored into SharedPreferences.
     * Adds click listeners to the buttons.
     *
     */
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
        if (!sharedPreferences.contains("locale")) {
            sharedPreferences.edit().putString("locale", "de").apply() //default germanFlag
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

    /**
     * Toast displayed when Username field is empty and submit has been clicked
     *
     */
    private fun userNameNeeded() {
        Toast.makeText(
            applicationContext,
            getString(R.string.username_needed),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast displayed when entered password are not matching when submit has been clicked
     *
     */
    private fun passwordsNotMatching() {
        Toast.makeText(
            applicationContext,
            getString(R.string.password_not_match),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast displayed when the password is shorter than 8 characters
     *
     */
    private fun passwordTooShort() {
        Toast.makeText(
            applicationContext,
            getString(R.string.password_too_short),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast displayed when no email format is recognized. "." and "@" are needed
     *
     */
    private fun emailNotValid() {
        Toast.makeText(
            applicationContext,
            getString(R.string.email_invalid),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast display when CheckBox with storage requirement has not been selected
     *
     */
    private fun needCheck() {
        Toast.makeText(
            applicationContext,
            getString(R.string.confirm_requirement),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Navigation to the next activity - the RegistrationConfirmationActivity
     *
     */
    fun nextStep() {
        val intent = Intent(this@RegistrationActivity, RegistrationConfirmationActivity::class.java)
        startActivity(intent)
    }


    /**
     * Toast that is displayed when a 422 HTTP error occurs. Main reason may be that the username is
     * already taken
     *
     */
    fun abort() {
        Toast.makeText(
            applicationContext,
            "Der Nutzername is bereits vergeben.",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Displayed Toast when an error occurs that is not further handled in this app.
     *
     */
    fun notifyError() {
        Toast.makeText(
            applicationContext,
            "Ein Fehler ist aufgetreten",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast displayed when no network connection is established and the request could not be sent
     *
     */
    fun noNetworkConnection() {
        Toast.makeText(
            applicationContext,
            "Kein Internet",
            Toast.LENGTH_LONG
        ).show()
    }
}
