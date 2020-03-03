package com.example.trackyourstress_ba.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.ui.home.HomeActivity
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    lateinit var loginButton: Button
    lateinit var backButton: Button
    lateinit var conUtils: ConnectionUtils
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        if (!preferences.contains("apiEndpoint")) {
            preferences.edit().putString("apiEndpoint", "https://api.trackyourstress.org").apply()
        }
        if (!preferences.contains("currentLanguage")) {
            preferences.edit().putString("currentLanguage", "de").apply() //default german
        }

        editUsername = findViewById(R.id.username)
        editPassword = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        backButton = findViewById(R.id.tohome_button_login)
        conUtils = ConnectionUtils()

        loginButton.setOnClickListener {
            if (editPassword.text.length >= 8 && editUsername.text.contains("@") &&
                editUsername.text.contains(".")
            ) {
                conUtils.loginUser(editUsername.text.toString(), editPassword.text.toString(), this)
            } else {
                if (editPassword.text.length < 8) {
                    Toast.makeText(
                        applicationContext,
                        "Passwort zu kurz. Bitte versuchen Sie es noch einmal",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
        backButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun notify401() {
        Toast.makeText(
            applicationContext,
            "Zugangsdaten ungültig. Bitte überprüfen Sie Ihre Eingaben",
            Toast.LENGTH_LONG
        ).show()
    }

    fun notify403() {
        Toast.makeText(
            applicationContext,
            "Nutzer wurde noch nicht verifiziert.",
            Toast.LENGTH_LONG
        ).show()
    }

    fun loginResponseReceived(email: String, response: JSONObject) {
        preferences.edit().putString("currentEmail", email).apply()
        val token = response.getJSONObject("data").getJSONObject("attributes").getString("token")
        preferences.edit().putString("token", token).apply()
        conUtils.getProfileData(this)

    }

    fun profileResponseReceived(response: JSONObject) {
        val userId = response.getJSONObject("data").getString("id")
        if (!preferences.contains("userId")) {
            preferences.edit().putString("userId", userId).apply()
        }
        val username = response.getJSONObject("data").getJSONObject("attributes").getString("name")
        if (!preferences.contains("userName")) {
            preferences.edit().putString("userName", username).apply() //default german
        }
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    fun notifyMissingData() {
        Toast.makeText(
            applicationContext,
            "Profildaten können nicht geladen werden. Bitte starten Sie die App neu",
            Toast.LENGTH_LONG
        ).show()
    }
}
