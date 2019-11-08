package com.example.trackyourstress_ba.kotlin

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
    }

    @Test
    fun logoutUser() {
    }
}