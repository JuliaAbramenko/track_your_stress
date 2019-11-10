package com.example.trackyourstress_ba.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.kotlin.GlobalVariables
import com.example.trackyourstress_ba.ui.home.HomeActivity
import org.json.JSONObject

//import com.example.trackyourstress_ba.kotlin.loginUser

class LoginActivity : AppCompatActivity() {

    lateinit var edit_username: EditText
    lateinit var edit_password : EditText
    lateinit var  login_button : Button
    lateinit var loading : ProgressBar
    lateinit var booltext : TextView
    lateinit var  back_button: Button
    lateinit var login_JSON: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        edit_username = findViewById(R.id.username)
        edit_password = findViewById(R.id.password)
        login_button= findViewById(R.id.login)
        loading = findViewById(R.id.loading)
        booltext = findViewById(R.id.textView)
        back_button = findViewById(R.id.tohome_button_login)
        val conUtils = ConnectionUtils()

        login_button.setOnClickListener {
            if(edit_password.text.length >= 8 && edit_username.text.contains("@") &&
                edit_username.text.contains(".")) {
                GlobalVariables.localStorage["current_email"] = edit_username.text.toString()
                conUtils.loginUser(edit_username.text.toString(), edit_password.text.toString(), this)
            }
        }
        back_button.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun response_received(response: JSONObject) {
        login_JSON = response
        GlobalVariables.localStorage["token"] = login_JSON.getJSONObject("data").getJSONObject("attributes").getString("token")
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }
}
