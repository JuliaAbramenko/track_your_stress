package com.example.trackyourstress_ba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.register.RegisterActivity

class MainActivity : AppCompatActivity() {
    lateinit var login_button : Button
    lateinit var register_button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login_button = findViewById<Button>(R.id.login_button)
        register_button = findViewById<Button>(R.id.register_button)


        login_button.setOnClickListener{
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
        }

        register_button.setOnClickListener{
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }


    }
}
