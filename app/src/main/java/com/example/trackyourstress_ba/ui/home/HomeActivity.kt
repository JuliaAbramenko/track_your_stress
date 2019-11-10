package com.example.trackyourstress_ba.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.GlobalVariables

class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

    override fun onStart() {
        super.onStart()
        toolbar = findViewById(R.id.toolbar)

    }


}
