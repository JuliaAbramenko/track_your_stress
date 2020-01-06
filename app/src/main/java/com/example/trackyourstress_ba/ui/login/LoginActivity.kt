package com.example.trackyourstress_ba.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.kotlin.GlobalVariables
import com.example.trackyourstress_ba.ui.home.HomeActivity
import org.json.JSONObject

//import com.example.trackyourstress_ba.kotlin.loginUser

class LoginActivity : AppCompatActivity() {

    lateinit var edit_username: EditText
    lateinit var edit_password : EditText
    lateinit var  login_button : Button
    lateinit var loading : ProgressBar
    lateinit var booltext : TextView
    lateinit var  back_button: Button
    lateinit var conUtils: ConnectionUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        edit_username = findViewById(R.id.username)
        edit_password = findViewById(R.id.password)
        login_button= findViewById(R.id.login)
        loading = findViewById(R.id.loading)
        back_button = findViewById(R.id.tohome_button_login)
        conUtils = ConnectionUtils()

        login_button.setOnClickListener {
            if(edit_password.text.length >= 8 && edit_username.text.contains("@") &&
                edit_username.text.contains(".")) {
                conUtils.loginUser(edit_username.text.toString(), edit_password.text.toString(), this)
            } else {
                if(edit_password.text.length < 8)
                    Toast.makeText(getApplicationContext(),
                        "Password not correct ",
                        Toast.LENGTH_LONG).show() //TODO catch errors
            }
        }
        back_button.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun login_response_received(response: JSONObject) {
        GlobalVariables.localStorage["token"] =
            response.getJSONObject("data").getJSONObject("attributes").getString("token")
        conUtils.get_profile_data(this)

    }

    fun profile_response_received(response: JSONObject) {
        val user_id = response.getJSONObject("data").getString("id")
        GlobalVariables.localStorage["user_id"] = user_id
        val username = response.getJSONObject("data").getJSONObject("attributes").getString("name")
        GlobalVariables.localStorage["username"] = username
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }
}
