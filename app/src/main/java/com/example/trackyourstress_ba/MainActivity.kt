package com.example.trackyourstress_ba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener{
            val intent = Intent(this@MainActivity,LoginActivity::class.java);
            startActivity(intent)
        }
    }
}
