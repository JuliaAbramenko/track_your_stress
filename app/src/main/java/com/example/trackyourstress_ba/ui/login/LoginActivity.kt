package com.example.trackyourstress_ba.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.ui.home.HomeActivity

//import com.example.trackyourstress_ba.kotlin.loginUser

class LoginActivity : AppCompatActivity() {

    lateinit var edit_username: EditText
    lateinit var edit_password : EditText
    lateinit var  login_button : Button
    lateinit var loading : ProgressBar
    lateinit var booltext : TextView
    lateinit var con_utils : ConnectionUtils
    lateinit var  back_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        edit_username = findViewById(R.id.username)
        edit_password = findViewById(R.id.password)
        login_button= findViewById(R.id.login)
        loading = findViewById(R.id.loading)
        booltext = findViewById(R.id.textView)
        con_utils = ConnectionUtils()
        back_button = findViewById(R.id.tohome_button_login)
        /*
        login_button.setOnClickListener {
            if(edit_password.text.length > 7 && edit_username.text.contains("@")) {
                con_utils.loginUser(edit_username.text.toString(), edit_password.text.toString(), this)
            }
            else {
                con_utils.logoutUser(this)

            }
        }
            */

        login_button.setOnClickListener {
            if(edit_password.text.length >= 8 && edit_username.text.contains("@") &&
                edit_username.text.contains(".")) {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        back_button.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }


    }
}
