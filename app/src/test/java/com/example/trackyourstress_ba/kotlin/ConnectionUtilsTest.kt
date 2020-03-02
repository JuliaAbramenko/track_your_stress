package com.example.trackyourstress_ba.kotlin

import android.content.Context
import com.example.trackyourstress_ba.ui.login.LoginActivity
import com.example.trackyourstress_ba.ui.register.RegisterActivity
import org.junit.Test

import org.junit.Assert.*

class ConnectionUtilsTest {

    @Test
    fun registerUser() {
        val email = "julileo@hotmail.de"
        val password = "testtest"
        val password_confirm = password
        val username = "SuperTest"
        val registerActivity = RegisterActivity()
        val con_utils = ConnectionUtils()
        con_utils.registerUser(email, password, password_confirm, username, registerActivity)
        //assert()
    }

    @Test
    fun loginUser() {
        val email = "julileo@hotmail.de"
        val password = "testtest"
        val connectionUtils = ConnectionUtils()
        val loginActivity = LoginActivity()
        connectionUtils.loginUser(email, password, loginActivity)
        val preferences = loginActivity.getSharedPreferences(
            loginActivity.packageName, Context.MODE_PRIVATE
        )

        assert(preferences.contains("token"))
        assert(preferences.contains("currentEmail"))
    }

    @Test
    fun logoutUser() {
    }
}