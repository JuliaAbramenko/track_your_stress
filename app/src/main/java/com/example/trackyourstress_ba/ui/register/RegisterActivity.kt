package com.example.trackyourstress_ba.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ConnectionUtils

class RegisterActivity : AppCompatActivity() {

    lateinit var edit_email: EditText
    lateinit var edit_password: EditText
    lateinit var edit_password_confirmation: EditText
    lateinit var edit_username: EditText
    lateinit var register_button: Button
    lateinit var con_utils : ConnectionUtils
    lateinit var test_text: TextView
    lateinit var back_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edit_email = findViewById(R.id.register_email_field)
        edit_password = findViewById(R.id.register_password_field)
        edit_password_confirmation = findViewById(R.id.register_password_confirmation_field)
        edit_username = findViewById(R.id.register_username_field)
        register_button = findViewById(R.id.register_ok_button)
        con_utils = ConnectionUtils()
        back_button = findViewById(R.id.tohome_button_reg)

        register_button.setOnClickListener {
            /*if(edit_email.text.contains("@") &&
                    editPassword.text.length > 7 && editPassword.text.equals(edit_password_confirmation.text) &&
                    edit_password_confirmation.text.length > 7 &&
                    editUsername.text.length > 7) {*/
                con_utils.registerUser(edit_email.text.toString(), edit_password.text.toString(),
                    edit_password_confirmation.text.toString(), edit_username.text.toString(), this)
            val intent = Intent(this@RegisterActivity, RegistrationConfirmationActivity::class.java)
            startActivity(intent)

            //}
        }

        back_button.setOnClickListener {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
        }


    }
}
