package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

import com.example.trackyourstress_ba.kotlin.ProfileUtils
import org.json.JSONObject
import java.lang.Exception
import android.widget.EditText
import com.example.trackyourstress_ba.R


class ProfileFragment: Fragment(){

    lateinit var editUsername: EditText
    lateinit var editEmail: EditText
    lateinit var editFirstName: EditText
    lateinit var editLastName: EditText
    lateinit var sexRadioGroup: RadioGroup
    lateinit var submitButton: Button
    lateinit var changePasswordButton: Button
    lateinit var deleteProfileButton: Button
    lateinit var profileUtils: ProfileUtils
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        currentContext = container!!.context
        profileUtils = ProfileUtils(this)
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onStart() {
        super.onStart()
        profileUtils.getProfile(this)
        editUsername = view!!.findViewById(R.id.edit_name)
        editEmail = view!!.findViewById(R.id.edit_email)
        editFirstName = view!!.findViewById(R.id.edit_firstname)
        editLastName = view!!.findViewById(R.id.edit_lastname)
        sexRadioGroup = view!!.findViewById(R.id.sex_radio_group)
        submitButton = view!!.findViewById(R.id.submit_button_update_profile)
        changePasswordButton = view!!.findViewById(R.id.button_change_password)
        deleteProfileButton = view!!.findViewById(R.id.button_delete_profile)

        submitButton.setOnClickListener {
                profileUtils.updateProfile(
                    editUsername.text.toString(), editFirstName.text.toString(),
                    editLastName.text.toString(),
                    getCurrentSexId(),
                    this
                )
            profileUpdated()
        }
        changePasswordButton.setOnClickListener {
            val alert = AlertDialog.Builder(currentContext, R.style.AlertDialogTheme)
            val oldPassword = EditText(currentContext)
            oldPassword.setTextColor(Color.BLACK)

            oldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            alert.setTitle("Passwort ändern")
            alert.setMessage("Altes Passwort")
            alert.setView(oldPassword)
            alert.setPositiveButton(
                "Weiter",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    //What ever you want to do with the value
                    callNewPasswordDialog()
                }
            )

            alert.setNegativeButton(
                "Abbrechen",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    Toast.makeText(currentContext, "Would not change", Toast.LENGTH_LONG).show()
                })

            alert.show()
        }

        deleteProfileButton.setOnClickListener {
            val builder = AlertDialog.Builder(currentContext, R.style.AlertDialogTheme)
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

    private fun profileUpdated() {
        Toast.makeText(context, "updated profile", Toast.LENGTH_LONG).show()
    }

    private fun callNewPasswordDialog() {
        val alert = AlertDialog.Builder(currentContext, R.style.AlertDialogTheme)
        val newPassword = EditText(currentContext)

        newPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        alert.setTitle("Passwort ändern")
        alert.setMessage("Neues Passwort")

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


    fun getCurrentSexId(): Int {
        when (sexRadioGroup.checkedRadioButtonId) {
            R.id.profile_sex_not_known -> return 0
            R.id.profile_male -> return 1
            R.id.profile_female -> return 2
            R.id.profile_not_applicable -> return 9
            else -> return 0
        }

    }

    fun responseReceived(response: JSONObject) {
        try {
            val profileJSON = response.getJSONObject("data").getJSONObject("attributes")
            val sharedPrefs = currentContext.getSharedPreferences(
                currentContext.packageName, Context.MODE_PRIVATE
            )
            sharedPrefs.edit().putString("userName", profileJSON.getString("name")).apply()
            sharedPrefs.edit().putString("userEmail", profileJSON.getString("email")).apply()
            sharedPrefs.edit().putString("firstName", profileJSON.getString("firstname")).apply()
            sharedPrefs.edit().putString("lastName", profileJSON.getString("lastname")).apply()
            sharedPrefs.edit().putString("sex", profileJSON.getString("sex")).apply()
            fillDataFields(sharedPrefs)
        } catch (except : Exception) {
        }

    }

    fun fillDataFields(sharedPrefs: SharedPreferences) {
        editUsername.setText(sharedPrefs.getString("userName", null))
        editEmail.setText(sharedPrefs.getString("userEmail", null))
        editFirstName.setText(sharedPrefs.getString("firstName", null))
        editLastName.setText(sharedPrefs.getString("lastName", null))
        when (sharedPrefs.getString("sex", null)) {
            "0" -> sexRadioGroup.check(R.id.profile_sex_not_known)
            "1" -> sexRadioGroup.check(R.id.profile_male)
            "2" -> sexRadioGroup.check(R.id.profile_female)
            "9" -> sexRadioGroup.check(R.id.profile_not_applicable)
        }
    }


    fun updatePasswordReceived(response: JSONObject) {
        try {

        } catch (except: Exception) {}

    }

    fun profileDeleted(response: JSONObject) {
        try {
            //GlobalVariables.localStorage.clear()
        } catch (except: Exception) {}

    }
}