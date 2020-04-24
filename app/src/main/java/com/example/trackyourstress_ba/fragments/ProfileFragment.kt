package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
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
    lateinit var profileUtils: ProfileUtils
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        currentContext = container!!.context
        profileUtils = ProfileUtils(this)
        this.activity?.title = getString(R.string.profile)
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onStart() {
        super.onStart()
        profileUtils.getProfile(this)
        editUsername = requireView().findViewById(R.id.edit_name)
        editEmail = requireView().findViewById(R.id.edit_email)
        editFirstName = requireView().findViewById(R.id.edit_firstname)
        editLastName = requireView().findViewById(R.id.edit_lastname)
        sexRadioGroup = requireView().findViewById(R.id.sex_radio_group)
        submitButton = requireView().findViewById(R.id.submit_button_update_profile)
        changePasswordButton = requireView().findViewById(R.id.button_change_password)
        submitButton.setOnClickListener {
                profileUtils.updateProfile(
                    editUsername.text.toString(), editFirstName.text.toString(),
                    editLastName.text.toString(),
                    getCurrentSexId(),
                    this
                )
        }
        changePasswordButton.setOnClickListener {
            callOldPasswordDialog()
        }
    }

    fun callOldPasswordDialog() {
        val alert = AlertDialog.Builder(currentContext, R.style.AlertDialogTheme)
        val oldPassword = EditText(currentContext)
        oldPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        oldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        alert.setTitle(getString(R.string.change_password))
        alert.setMessage(getString(R.string.old_password))
        alert.setView(oldPassword)
        alert.setPositiveButton(
            getString(R.string.go_on)
        ) { _, _ ->
            checkOldPassword(oldPassword.text.toString())
        }
        alert.setNegativeButton(
            getString(R.string.cancel)
        ) { _, _ ->
            Toast.makeText(
                currentContext,
                getString(R.string.password_has_not_been_changed),
                Toast.LENGTH_LONG
            ).show()
        }

        alert.show()
    }

    private fun checkOldPassword(oldPassword: String) {
        profileUtils.repeatLogin(oldPassword, this)
    }

    private fun profileUpdated() {
        Toast.makeText(context, getString(R.string.profile_updated), Toast.LENGTH_LONG).show()
    }

    fun callNewPasswordDialog() {
        val alert = AlertDialog.Builder(currentContext, R.style.AlertDialogTheme)
        val newPassword = EditText(currentContext)
        val newPasswordConfirmation = EditText(currentContext)
        newPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        newPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        newPasswordConfirmation.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        newPasswordConfirmation.transformationMethod = PasswordTransformationMethod.getInstance()
        val confirmTextView = TextView(currentContext)
        confirmTextView.text = getString(R.string.password_confirm)
        val linearLayout = LinearLayout(currentContext)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(newPassword)
        linearLayout.addView(confirmTextView)
        linearLayout.addView(newPasswordConfirmation)
        alert.setTitle(getString(R.string.change_password))
        alert.setMessage(getString(R.string.new_password))
        alert.setView(linearLayout)
        alert.setPositiveButton(
            getString(R.string.go_on)

        ) { _, _ ->
            if (newPassword.text.toString() == newPasswordConfirmation.text.toString()) {
                profileUtils.updatePasswordOptions(newPassword.text.toString(), this)
            } else {
                showNotMatching()
                callNewPasswordDialog()
            }
        }

        alert.setNegativeButton(
            getString(R.string.cancel)
        ) { _, _ ->
            Toast.makeText(
                currentContext,
                getString(R.string.password_has_not_been_changed),
                Toast.LENGTH_LONG
            ).show()
        }

        alert.show()
    }

    private fun showNotMatching() {
        Toast.makeText(
            currentContext,
            getString(R.string.password_not_match),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getCurrentSexId(): Int {
        return when (sexRadioGroup.checkedRadioButtonId) {
            R.id.profile_sex_not_known -> 0
            R.id.profile_male -> 1
            R.id.profile_female -> 2
            R.id.profile_not_applicable -> 9
            else -> 0
        }
    }

    fun responseReceived(response: JSONObject) {
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
    }

    private fun fillDataFields(sharedPrefs: SharedPreferences) {
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
        profileUpdated()
    }

    fun updatePasswordReceived() {
        Toast.makeText(
            currentContext,
            getString(R.string.password_has_been_changed),
            Toast.LENGTH_LONG
        ).show()
    }

    fun updatePasswordOptionsReceived(newPassword: String) {
        profileUtils.updatePassword(newPassword, this)
    }

    fun abortPasswordChange() {
        Toast.makeText(
            currentContext,
            getString(R.string.please_retry),
            Toast.LENGTH_LONG
        ).show()
    }

    fun notifyNetworkError() {
        Toast.makeText(
            currentContext,
            R.string.profile_not_loaded,
            Toast.LENGTH_LONG
        ).show()
    }

    fun notifyServerError() {
        Toast.makeText(
            currentContext,
            getString(R.string.server_error_occured),
            Toast.LENGTH_LONG
        ).show()
    }

}