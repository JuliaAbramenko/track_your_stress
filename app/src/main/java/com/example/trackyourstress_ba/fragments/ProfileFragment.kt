package com.example.trackyourstress_ba.fragments

import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.GlobalVariables
import com.example.trackyourstress_ba.kotlin.ProfileUtils
import org.json.JSONObject
import java.lang.Exception

class ProfileFragment: Fragment(){

    lateinit var edit_username : EditText
    lateinit var edit_email : EditText
    lateinit var edit_firstname : EditText
    lateinit var edit_lastname : EditText
    lateinit var sex_radio_group : RadioGroup
    lateinit var submit_button : Button
    lateinit var change_password_button : Button
    lateinit var delete_profile_button : Button
    lateinit var profileUtils: ProfileUtils


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        profileUtils = ProfileUtils()
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onStart() {
        super.onStart()
        profileUtils.getProfile(this)
        edit_username = view!!.findViewById(R.id.edit_name)
        edit_email = view!!.findViewById(R.id.edit_email)
        edit_firstname = view!!.findViewById(R.id.edit_firstname)
        edit_lastname = view!!.findViewById(R.id.edit_lastname)
        sex_radio_group = view!!.findViewById(R.id.sex_radio_group)
        submit_button = view!!.findViewById(R.id.submit_button_update_profile)
        change_password_button = view!!.findViewById(R.id.button_change_password)
        delete_profile_button = view!!.findViewById(R.id.button_delete_profile)

        submit_button.setOnClickListener {
                profileUtils.updateProfile(
                    edit_username.text.toString(), edit_firstname.text.toString(),
                    edit_lastname.text.toString(),
                    get_current_sex_id(),
                    this
                )
            Toast.makeText(context, "updated profile", Toast.LENGTH_LONG).show()
        }

        change_password_button.setOnClickListener {
            //profileUtils.updataPassword( )
            //TODO dialog window with 'are you sure?', 'New password, confirm new password'..
        }

    }


    fun get_current_sex_id(): Int {
        when (sex_radio_group.checkedRadioButtonId) {
            R.id.profile_sex_not_known -> return 0
            R.id.profile_male -> return 1
            R.id.profile_female -> return 2
            R.id.profile_not_applicable -> return 9
            else -> return 0
        }

    }
    fun response_received(response: JSONObject){
        try {
            val profile_JSON_attributes = response.getJSONObject("data").getJSONObject("attributes")
            GlobalVariables.localStorage["username"] = profile_JSON_attributes.getString("name")
            GlobalVariables.localStorage["current_email"] = profile_JSON_attributes.getString("email")
            GlobalVariables.localStorage["first_name"] = profile_JSON_attributes.getString("firstname")
            GlobalVariables.localStorage["last_name"] = profile_JSON_attributes.getString("lastname")
            GlobalVariables.localStorage["sex"] = profile_JSON_attributes.getString("sex")
            fill_data_fields()
        } catch (except : Exception) {
        }

    }

    fun fill_data_fields () {
        edit_username.setText( GlobalVariables.localStorage["username"])
        edit_email.setText(GlobalVariables.localStorage["current_email"])
        edit_firstname.setText(GlobalVariables.localStorage["first_name"])
        edit_lastname.setText(GlobalVariables.localStorage["last_name"])
        when(GlobalVariables.localStorage["sex"]) {
            "0" -> sex_radio_group.check(R.id.profile_sex_not_known)
            "1" -> sex_radio_group.check(R.id.profile_male)
            "2" -> sex_radio_group.check(R.id.profile_female)
            "9" -> sex_radio_group.check(R.id.profile_not_applicable)
        }
    }

    fun update_received(response : JSONObject) {
        try {
            val profile_JSON_attributes = response.getJSONObject("data").getJSONObject("attributes")
            GlobalVariables.localStorage["username"] = profile_JSON_attributes.getString("name")
            GlobalVariables.localStorage["current_email"] = profile_JSON_attributes.getString("email")
            GlobalVariables.localStorage["first_name"] = profile_JSON_attributes.getString("firstname")
            GlobalVariables.localStorage["last_name"] = profile_JSON_attributes.getString("lastname")
            GlobalVariables.localStorage["sex"] = profile_JSON_attributes.getString("sex")
            fill_data_fields()
        } catch (except: Exception) {}
    }

    fun update_password_received(response: JSONObject) {
        try {

        } catch (except: Exception) {}

    }

    fun profile_deleted(response: JSONObject) {
        try {
            GlobalVariables.localStorage.clear()
        } catch (except: Exception) {}

    }
}