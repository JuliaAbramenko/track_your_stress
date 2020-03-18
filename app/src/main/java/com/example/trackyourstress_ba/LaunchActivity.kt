package com.example.trackyourstress_ba

import android.content.Intent
import com.example.trackyourstress_ba.ui.home.HomeActivity
import android.os.Bundle
import android.app.Activity
import android.content.Context


class LaunchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Runtime.getRuntime().exec("logcat -f" + " /sdcard/Logcat.txt")
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        if (!sharedPreferences.contains("token")) {
            sharedPreferences.edit().putString("token", null).apply()
        }
        if (sharedPreferences.contains("token") && sharedPreferences.getString(
                "token",
                null
            ) != null
        ) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
            startActivity(intent)
        }
    }
}