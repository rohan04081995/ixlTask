package com.example.ixltask.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ixltask.R
import com.example.ixltask.models.BankInfo
import com.example.ixltask.models.EmployeeInfo
import com.example.ixltask.models.PersonalInfo
import com.example.ixltask.ui.EmployeeInfoActivity.Companion.TAG

class PersonalInfoActivity : AppCompatActivity() {

    companion object {
        val PARCELABLE_KEY = "parcelKey"
        val USER_ID_KEY = "userIdKeyX"
    }

    private lateinit var personalInfoToolbar: Toolbar
    private lateinit var firstNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var phoneNoEt: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var dobEt: EditText
    private lateinit var submitPInfoButton: Button

    var selectedRadioButtonId: RadioButton? = null
    private var personalInfo: PersonalInfo? = null
    private var radioId: Int = 0
    /*0 for male 1 for female*/

    private lateinit var maleRb: RadioButton
    private lateinit var femaleRb: RadioButton
    private var userid: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        personalInfoToolbar = findViewById(R.id.personalInfoToolbar)
        firstNameEt = findViewById(R.id.firstNameEt)
        lastNameEt = findViewById(R.id.lastNameEt)
        phoneNoEt = findViewById(R.id.phoneNoEt)
        radioGroup = findViewById(R.id.radioGroup)
        dobEt = findViewById(R.id.dobEt)
        submitPInfoButton = findViewById(R.id.submitPInfoButton)
        maleRb = findViewById(R.id.maleRb)
        femaleRb = findViewById(R.id.femaleRb)


        personalInfo = intent.getParcelableExtra<PersonalInfo>(PersonalInfoActivity.PARCELABLE_KEY)
        userid = intent.getIntExtra(PersonalInfoActivity.USER_ID_KEY, -1)

        Log.d(TAG, "onCreate: personalinfo: ${personalInfo.toString()}")
        if (userid == -1) {
            personalInfo = PersonalInfo(employeeInfo = EmployeeInfo(), bankInfo = BankInfo())
        } else {
            firstNameEt.setText(personalInfo!!.firstName)
            lastNameEt.setText(personalInfo!!.lastName)
            phoneNoEt.setText(personalInfo!!.phoneNo)
            radioId = personalInfo!!.gender
            if (radioId == 0) {
                maleRb.isChecked = true
                selectedRadioButtonId = maleRb
            } else {
                femaleRb.isChecked = true
                selectedRadioButtonId = femaleRb
            }
            dobEt.setText(personalInfo!!.dob)
        }


        submitPInfoButton.setOnClickListener(View.OnClickListener {
            Log.d(EmployeeInfoActivity.TAG, "onCreate:pi: checkAllFields: ${checkAllFields()}")
            if (!checkAllFields()) {

                return@OnClickListener
            } else {

                personalInfo!!.firstName = firstNameEt.text.toString().trim()
                personalInfo!!.lastName = lastNameEt.text.toString().trim()
                personalInfo!!.phoneNo = phoneNoEt.text.toString().trim()
                personalInfo!!.gender = radioId
                personalInfo!!.dob = dobEt.text.toString().trim()

                val intent = Intent(this, EmployeeInfoActivity::class.java)
                intent.putExtra(PARCELABLE_KEY, personalInfo)
                intent.putExtra(USER_ID_KEY, userid)
                intent.putExtra(USER_ID_KEY, userid)
                startActivity(intent)
            }
        })

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedRadioButtonId = findViewById(checkedId)
            if (selectedRadioButtonId!!.id == R.id.maleRb) {
                radioId = 0
            } else {
                radioId = 1
            }
        }
    }

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