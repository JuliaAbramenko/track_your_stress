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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val edit_username = findViewById<EditText>(R.id.username)
        val edit_password = findViewById<EditText>(R.id.password)
        val login_button= findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val booltext = findViewById<TextView>(R.id.textView)
        val conUtils = ConnectionUtils()

        login_button.setOnClickListener {
            if(edit_password.text.length > 7 && edit_username.text.contains("@")) {
                val response = conUtils.loginUser(edit_username.text.toString(), edit_password.text.toString(), booltext)

                //val token = json.getString("data.attributes.token")
                /*val token = response
                if (token.toString().length > 1) {
                    GlobalVariables.localStorage.put("token", token.toString())
                    booltext.setText("token aquired:" + token.toString())
                }*/
            }
        }

    }
}
