package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

import com.example.trackyourstress_ba.kotlin.GlobalVariables
import com.example.trackyourstress_ba.kotlin.ProfileUtils
import org.json.JSONObject
import java.lang.Exception
import android.widget.EditText
import com.example.trackyourstress_ba.R
import kotlinx.android.synthetic.main.activity_login.*


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
    lateinit var currentContext: Context



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        currentContext = container!!.context
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
        //TODO Modify dialog
        change_password_button.setOnClickListener {
            val alert = AlertDialog.Builder(currentContext)
            val oldPassword = EditText(currentContext)

            oldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            alert.setMessage("Change Password")
            alert.setTitle("Enter old password")
            alert.setView(oldPassword)

            alert.setPositiveButton("Continue",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    //What ever you want to do with the value
                    callNewPasswordDialog()
                }

            )

            alert.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    Toast.makeText(currentContext, "Would not change", Toast.LENGTH_LONG).show()
                })

            alert.show()
        }

        delete_profile_button.setOnClickListener {
            val builder = AlertDialog.Builder(currentContext)
            builder.setTitle("DELETE PROFILE")
            builder.setMessage("Do you want to delete your profile?")
            builder.setPositiveButton("YES") { dialog, which ->
                Toast.makeText(
                    currentContext,
                    "This would delete your profile.",
                    Toast.LENGTH_SHORT
                ).show()
                //TODO and to test
                //profileUtils.deleteProfile(this)

            }
            builder.setNegativeButton("No") { dialog, which ->
                Toast.makeText(currentContext, "Your profile was not deleted", Toast.LENGTH_SHORT)
                    .show()
            }

            builder.setNeutralButton("Cancel") { _, _ ->
            }
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }


    }

    private fun callNewPasswordDialog() {
        val alert = AlertDialog.Builder(currentContext)
        val newPassword = EditText(currentContext)

        newPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        alert.setMessage("Change Password")
        alert.setTitle("Enter new password")
        alert.setView(newPassword)

        alert.setPositiveButton("Continue",
            DialogInterface.OnClickListener { dialog, whichButton ->
                //What ever you want to do with the value
                val new = newPassword.text.toString()
                Toast.makeText(currentContext, "this would update password", Toast.LENGTH_LONG)
                    .show()
                //profileUtils.updatePassword(new, this)

            }

        )

        alert.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, whichButton ->
                Toast.makeText(currentContext, "Would not change", Toast.LENGTH_LONG).show()
            })

        alert.show()
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