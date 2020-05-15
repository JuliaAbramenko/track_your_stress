package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ProfileUtils
import org.json.JSONObject

/**
 * The class managing the ProfileFragment in the navigation drawer
 */
class ProfileFragment: Fragment(){
    private lateinit var editUsername: EditText
    private lateinit var editEmail: EditText
    private lateinit var editFirstName: EditText
    private lateinit var editLastName: EditText
    private lateinit var sexRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var changePasswordButton: Button
    private lateinit var profileUtils: ProfileUtils
    lateinit var currentContext: Context
    lateinit var sharedPreferences: SharedPreferences

    /**
     * general creation method for the ProfileFragment. Is called before it is displayed.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        currentContext = container!!.context
        profileUtils = ProfileUtils(this)
        this.activity?.title = getString(R.string.profile)
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    /**
     * Called at the actual display of the View. Text fields that are changeable are located. Also sets up  Button click listeners
     * for profile updates and password change options.
     */
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
        submitButton.setOnClickListener {
            profileUtils.updateProfile(
                editUsername.text.toString(), editFirstName.text.toString(),
                editLastName.text.toString(),
                getCurrentSexId(), this,
                true
            )
        }
        changePasswordButton.setOnClickListener {
            callOldPasswordDialog()
        }
    }

    /**
     * Initial Dialog creation that is displayed when the user clicks the changePasswordButton. Old password needed before a login
     * can be retried.
     */
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

    /**
     * Verification if the entered password is correct with another login try
     */
    private fun checkOldPassword(oldPassword: String) {
        profileUtils.repeatLogin(oldPassword, this)
    }

    /**
     * Message display that a profile update has been successful.
     */
    private fun profileUpdated() {
        Toast.makeText(context, getString(R.string.profile_updated), Toast.LENGTH_LONG).show()
    }

    /**
     * Next Dialog displayed when the login retry has been successful.
     * Here, the new password can be entered and confirmed.
     */
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

    /**
     * Toast display to the user if both entered password for the password change do not match.
     */
    private fun showNotMatching() {
        Toast.makeText(
            currentContext,
            getString(R.string.password_not_match),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Assignment from a RadioGroup selection to the gender code used by the platform.
     */
    private fun getCurrentSexId(): Int {
        return when (sexRadioGroup.checkedRadioButtonId) {
            R.id.profile_sex_not_known -> 0
            R.id.profile_male -> 1
            R.id.profile_female -> 2
            R.id.profile_not_applicable -> 9
            else -> 0
        }
    }

    /**
     * Extraction of profile values that are displayed to the user when the profile retrieval worked out.
     */
    fun responseReceived(response: JSONObject, update: Boolean) {
        val profileJSON = response.getJSONObject("data").getJSONObject("attributes")
        val sharedPrefs = currentContext.getSharedPreferences(
            currentContext.packageName, Context.MODE_PRIVATE
        )
        sharedPrefs.edit().putString("userName", profileJSON.getString("name")).apply()
        sharedPrefs.edit().putString("userEmail", profileJSON.getString("email")).apply()
        sharedPrefs.edit().putString("firstName", profileJSON.getString("firstname")).apply()
        sharedPrefs.edit().putString("lastName", profileJSON.getString("lastname")).apply()
        sharedPrefs.edit().putString("sex", profileJSON.getString("sex")).apply()
        fillDataFields(sharedPrefs, update)
    }

    /**
     * Assignment of the extracted values from a successful profile data retrieval to the GUI elements
     * displayed to the user.
     */
    private fun fillDataFields(sharedPrefs: SharedPreferences, update: Boolean) {
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
        if (update) profileUpdated()
    }

    /**
     * Toast displayed to the user when password change has been successful.
     */
    fun updatePasswordReceived() {
        Toast.makeText(
            currentContext,
            getString(R.string.password_has_been_changed),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Prior to a password update, an OPTIONS request has to be sent with the new password.
     */
    fun updatePasswordOptionsReceived(newPassword: String) {
        profileUtils.updatePassword(newPassword, this)
    }

    /**
     * If a login retry fails, the second Dialog is not displayed, but the first again and the user
     * is notified that the password he entered has been wrong, requiring a retry.
     */
    fun abortPasswordChange() {
        Toast.makeText(
            currentContext,
            getString(R.string.please_retry),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Taost display to the user if a network error has occured when clicking on one of the buttons.
     */
    fun notifyNetworkError() {
        Toast.makeText(
            currentContext,
            R.string.profile_not_loaded,
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Toast display to the user if a server error has occured when clicking on the buttons or profile
     * value retrieving has failed.
     */
    fun notifyServerError() {
        Toast.makeText(
            currentContext,
            getString(R.string.server_error_occured),
            Toast.LENGTH_LONG
        ).show()
    }

}