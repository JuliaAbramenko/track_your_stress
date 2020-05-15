package com.example.trackyourstress_ba.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.ui.start.StartActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.Utils.ConnectionUtils
import com.example.trackyourstress_ba.ui.home.HomeActivity
import org.json.JSONObject

/**
 * The activity that handles the login. To get here, the login button in the StartActivity has
 * to be used.
 *
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var backButton: Button
    private lateinit var conUtils: ConnectionUtils
    lateinit var sharedPreferences: SharedPreferences

    /**
     * general creation method for the LoginActivity. Identification of relevant fields and buttons
     * are made as well relevant values "apiEndpoint" and "locale" are stored into SharedPreferences
     * Adds click listeners to the buttons.
     *
     */
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
        if (!sharedPreferences.contains("locale")) {
            sharedPreferences.edit().putString("locale", "de").apply() //default germanFlag
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

    /**
     * Toast displayed to the user if inputs are not valid
     *
     */
    fun notify401() {
        Toast.makeText(
            applicationContext,
            R.string.notify401,
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast displayed to the user if the user is not verified
     *
     */
    fun notify403() {
        Toast.makeText(
            applicationContext,
            R.string.notify403,
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Function invoked when a successful login has been performed by the ConnectionsUtils.
     *
     * @param email with which email the login has been performed
     * @param response JSON with token to extract and save into SharedPreferences
     */
    fun loginResponseReceived(email: String, response: JSONObject) {
        sharedPreferences.edit().putString("userEmail", email).apply()
        val token = response.getJSONObject("data").getJSONObject("attributes").getString("token")
        sharedPreferences.edit().putString("token", token).commit()
        conUtils.getProfileData(this)

    }

    /**
     * Function that is called when a successful login has been performed, the token saved and profile information
     * to display in the navigation drawer in the HomeActivity is fetched.
     *
     * @param response raw JSON for profile information
     */
    fun profileResponseReceived(response: JSONObject) {
        val userId = response.getJSONObject("data").getString("id")
        sharedPreferences.edit().putString("userId", userId).apply()
        val username = response.getJSONObject("data").getJSONObject("attributes").getString("name")
        sharedPreferences.edit().putString("userName", username).apply() //default germanFlag
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    /**
     * Toast displayed when profile JSON cannot be get
     *
     */
    fun notifyMissingData() {
        Toast.makeText(
            applicationContext,
            R.string.profile_not_loaded,
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast displayed when a network error occurs
     *
     */
    fun notifyNetworkError() {
        Toast.makeText(
            applicationContext,
            R.string.network_error,
            Toast.LENGTH_LONG
        ).show()
    }
}
