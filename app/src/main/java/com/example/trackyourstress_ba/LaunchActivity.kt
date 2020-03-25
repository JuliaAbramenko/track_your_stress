package com.example.trackyourstress_ba

import android.content.Intent
import com.example.trackyourstress_ba.ui.home.HomeActivity
import android.os.Bundle
import android.app.Activity
import android.content.Context


class LaunchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        //sharedPreferences.edit().clear().apply()
        /*if (!sharedPreferences.contains("token")) {
            sharedPreferences.edit().putString("token", "").apply()
        }*/
        if (sharedPreferences.contains("token") && sharedPreferences.getString(
                "token",
                ""
            ) != ""
        ) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}