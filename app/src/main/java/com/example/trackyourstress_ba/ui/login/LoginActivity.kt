package com.example.trackyourstress_ba.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.StartActivity
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
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                        R.string.password_short,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        backButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, StartActivity::class.java)
            startActivity(intent)
        }
    }

    fun notify401() {
        Toast.makeText(
            applicationContext,
            R.string.notify401,
            Toast.LENGTH_LONG
        ).show()
    }

    fun notify403() {
        Toast.makeText(
            applicationContext,
            R.string.notify403,
            Toast.LENGTH_LONG
        ).show()
    }

    fun loginResponseReceived(email: String, response: JSONObject) {
        sharedPreferences.edit().putString("userEmail", email).apply()
        val token = response.getJSONObject("data").getJSONObject("attributes").getString("token")
        sharedPreferences.edit().putString("token", token).commit()
        conUtils.getProfileData(this)

    }

    fun profileResponseReceived(response: JSONObject) {
        val userId = response.getJSONObject("data").getString("id")
        sharedPreferences.edit().putString("userId", userId).apply()
        val username = response.getJSONObject("data").getJSONObject("attributes").getString("name")
        sharedPreferences.edit().putString("userName", username).apply() //default germanFlag
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    fun notifyMissingData() {
        Toast.makeText(
            applicationContext,
            R.string.profile_not_loaded,
            Toast.LENGTH_LONG
        ).show()
    }

    fun notifyNetworkError() {
        Toast.makeText(
            applicationContext,
            R.string.network_error,
            Toast.LENGTH_LONG
        ).show()
    }
}
