package com.example.trackyourstress_ba.kotlin

import android.content.Context

class ClearingUtils {

    companion object {
        fun clearSharedPreferences(context: Context) {
            val sharedPreferences = context.getSharedPreferences(
                context.packageName, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().clear().apply()
        }
    }


}