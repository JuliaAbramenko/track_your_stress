package com.example.trackyourstress_ba.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        edit_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val new_email = edit_email.text

            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }



        })
        edit_firstname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val new_firstname = edit_firstname.text
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}

        })
        edit_lastname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val new_lastname = edit_lastname.text
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}

        })

        sex_radio_group.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                val new_sex : String
                val id_chosen = sex_radio_group.checkedRadioButtonId
                when (id_chosen) {

                    R.id.profile_sex_not_known -> new_sex = "0"
                    R.id.profile_male -> new_sex = "1"
                    R.id.profile_female -> new_sex = "2"
                    R.id.profile_not_applicable -> new_sex="9"
                }
            } //TODO?? Button, Submit?

        })
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

        } catch (except: Exception) {}
    }

    fun update_password_received(response: JSONObject) {
        try {

        } catch (except: Exception) {}

    }

    fun profile_deleted(response: JSONObject) {
        try {

        } catch (except: Exception) {}

    }
}