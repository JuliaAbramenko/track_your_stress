package com.example.trackyourstress_ba.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
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
    var bool_received = false
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
        if(bool_received) {
            fill_data_fields()
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
            bool_received = true
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
}