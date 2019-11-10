package com.example.trackyourstress_ba.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.GlobalVariables

class HomeActivity : AppCompatActivity() {

    lateinit var yourtoken: TextView
    lateinit var youremail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        yourtoken = findViewById(R.id.yourtoken)
        youremail = findViewById(R.id.youremail)

    }

    override fun onStart() {
        super.onStart()
        yourtoken.text = GlobalVariables.localStorage["token"]
        youremail.text = GlobalVariables.localStorage["current_email"]
    }


}
