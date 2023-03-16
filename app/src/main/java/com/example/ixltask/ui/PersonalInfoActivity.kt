package com.example.ixltask.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ixltask.R
import com.example.ixltask.UtilsClass.Companion.validateEditFields
import com.example.ixltask.models.PersonalInfo
import com.example.ixltask.ui.EmployeeInfoActivity.Companion.TAG

class PersonalInfoActivity : AppCompatActivity() {

    companion object {
        public val PARCELABLE_KEY = "parcelKey"
    }

    private lateinit var personalInfoToolbar: Toolbar
    private lateinit var firstNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var phoneNoEt: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var dobEt: EditText
    private lateinit var submitPInfoButton: Button

    var selectedRadioButtonId: RadioButton? = null
    private lateinit var personalInfo: PersonalInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)
        personalInfo = intent.extras.getParcelable(PersonalInfoActivity.PARCELABLE_KEY,PersonalInfo::class.java)

        personalInfoToolbar = findViewById(R.id.personalInfoToolbar)
        firstNameEt = findViewById(R.id.firstNameEt)
        lastNameEt = findViewById(R.id.lastNameEt)
        phoneNoEt = findViewById(R.id.phoneNoEt)
        radioGroup = findViewById(R.id.radioGroup)
        dobEt = findViewById(R.id.dobEt)
        submitPInfoButton = findViewById(R.id.submitPInfoButton)


        submitPInfoButton.setOnClickListener(View.OnClickListener {
            Log.d(EmployeeInfoActivity.TAG, "onCreate:pi: checkAllFields: ${checkAllFields()}")
            if (!checkAllFields()) {

                return@OnClickListener
            } else {

                val intent = Intent(this, EmployeeInfoActivity::class.java)
                intent.putExtra(PARCELABLE_KEY, personalInfo)
                startActivity(intent)
            }
        })

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedRadioButtonId = findViewById(checkedId)
            Toast.makeText(this, selectedRadioButtonId?.text, Toast.LENGTH_SHORT).show()
        }
    }

    /* fun validateEditFields(editText: EditText, msg: String): Boolean {
         if (editText.text.toString().trim().isEmpty()) {
             editText.error = msg
             Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
             return false
         } else {
             editText.error = null
             return false
         }
     }*/
//
//    fun validateRadioButtonSelection(): Boolean {
//        if (selectedRadioButtonId == null) {
//            Toast.makeText(this, "please select gender", Toast.LENGTH_SHORT).show()
//            return false
//        } else {
//            return true
//        }
//    }
//
//    fun validatePhoneNo(): Boolean {
//        if (phoneNoEt.text.toString().trim().length < 10) {
//            phoneNoEt.error = "please enter valid phone no"
//            Toast.makeText(this, "please enter valid phone no", Toast.LENGTH_SHORT).show()
//            return false
//        } else {
//            return true
//        }
//    }

    private fun checkAllFields(): Boolean {
        if (firstNameEt.text.toString().trim().isEmpty()) {
            firstNameEt.setError("please enter valid first name")
            Toast.makeText(this, "please enter valid first name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (lastNameEt.text.toString().trim().isEmpty()) {
            lastNameEt.setError("please enter valid last name")
            Toast.makeText(this, "please enter valid last name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phoneNoEt.text.toString().trim().length < 10) {
            phoneNoEt.error = "please enter valid phone no"
            Toast.makeText(this, "please enter valid phone no", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedRadioButtonId == null) {
            Toast.makeText(this, "please select gender", Toast.LENGTH_SHORT).show()
            return false
        }
        if (dobEt.text.toString().trim().isEmpty()) {
            dobEt.setError("please enter valid dob")
            Toast.makeText(this, "please enter valid dob", Toast.LENGTH_SHORT).show()
            return false
        }
        // after all validation return true.
        firstNameEt.error = null
        lastNameEt.error = null
        phoneNoEt.error = null
        dobEt.error = null
        return true
    }
}