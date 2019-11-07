package com.example.trackyourstress_ba.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils

//import com.example.trackyourstress_ba.kotlin.loginUser

class LoginActivity : AppCompatActivity() {

    lateinit var edit_username: EditText
    lateinit var edit_password : EditText
    lateinit var  login_button : Button
    lateinit var loading : ProgressBar
    lateinit var booltext : TextView
    lateinit var conUtils : ConnectionUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        edit_username = findViewById<EditText>(R.id.username)
        edit_password = findViewById<EditText>(R.id.password)
        login_button= findViewById<Button>(R.id.login)
        loading = findViewById<ProgressBar>(R.id.loading)
        booltext = findViewById<TextView>(R.id.textView)
        conUtils = ConnectionUtils()

        login_button.setOnClickListener {
            if(edit_password.text.length > 7 && edit_username.text.contains("@")) {
                val response = conUtils.loginUser(edit_username.text.toString(), edit_password.text.toString(), this)
            }
            else {
                val response = conUtils.logoutUser(edit_username.text.toString())

            }
        }

    }
}
