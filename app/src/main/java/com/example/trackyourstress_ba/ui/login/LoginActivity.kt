package com.example.trackyourstress_ba.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username).toString()
        val password = findViewById<EditText>(R.id.password).toString()
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val booltext = findViewById<TextView>(R.id.textView)

        /*val response = loginUser(username, password)
        val json = response.jsonObject
        val token = json.getString("data.attributes.token")
        if(token != null) {
            GlobalVariables.localStorage.put("token", token)
            booltext.setText("token aquired:" + token)
        }*/


    }
}
