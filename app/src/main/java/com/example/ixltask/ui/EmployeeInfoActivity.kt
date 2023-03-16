package com.example.ixltask.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.ixltask.R
import com.example.ixltask.UtilsClass
import com.example.ixltask.models.EmployeeInfo
import com.example.ixltask.models.PersonalInfo

class EmployeeInfoActivity : AppCompatActivity() {

    companion object {
        public val TAG = "userData"
    }

    private lateinit var empInfoToolbar: Toolbar
    private lateinit var empNoEt: EditText
    private lateinit var empNameEt: EditText
    private lateinit var empDesignationEt: EditText
    private lateinit var accountTypeSpinner: Spinner
    private lateinit var workExpSpinner: Spinner
    private lateinit var submitEmpButton: Button

    private var accountType: String? = null
    private var selectedAccountTypeId: Int = -1

    private var totalWorkExp: String? = null
    private var selectedTotalWorkExp: Int = -1

    private lateinit var accountTypeList: List<String>
    private lateinit var totalExpList: List<String>
    private lateinit var personalInfo: PersonalInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_info)



        empInfoToolbar = findViewById(R.id.empInfoToolbar)
        empNoEt = findViewById(R.id.empNoEt)
        empNameEt = findViewById(R.id.empNameEt)
        empDesignationEt = findViewById(R.id.empDesignationEt)
        accountTypeSpinner = findViewById(R.id.accountTypeSpinner)
        workExpSpinner = findViewById(R.id.workExpSpinner)
        submitEmpButton = findViewById(R.id.submitEmpButton)

        accountTypeList = resources.getStringArray(R.array.accountTypeArray).toList()
        totalExpList = resources.getStringArray(R.array.totalExpArray).toList()


        val accountAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, accountTypeList)
        accountTypeSpinner.adapter = accountAdapter

        accountTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedAccountTypeId = position
                accountType = accountTypeList[position]

                Log.d(
                    TAG,
                    "onCreate:selectedAccountTypeId: $selectedAccountTypeId :: accountType: $accountType"
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        val expAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, totalExpList)
        workExpSpinner.adapter = expAdapter

        workExpSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedTotalWorkExp = position
                totalWorkExp = totalExpList[position]

                Log.d(
                    TAG,
                    "onCreate:selectedTotalWorkExp: $selectedTotalWorkExp :: totalWorkExp: $totalWorkExp"
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        submitEmpButton.setOnClickListener(View.OnClickListener {
//
//            if (UtilsClass.validateEditFields(
//                    this, empNoEt, "please enter valid employee no"
//                ) or !UtilsClass.validateEditFields(
//                    this, empNameEt, "please enter valid employee name"
//                ) or !UtilsClass.validateEditFields(
//                    this, empDesignationEt, "please enter valid designation"
//                ) or !UtilsClass.validateSpinnerData(
//                    this, selectedAccountTypeId, "please select valid account type"
//                ) or !UtilsClass.validateSpinnerData(
//                    this, selectedTotalWorkExp, "please select your experience"
//                )
//            )
            if (!checkAllFields()) {
                return@OnClickListener
            } else {
                Log.d(TAG, "onCreate: emp: account type: ${accountType} :: exp: $totalWorkExp")
                val intent = Intent(this, BankInfoActivity::class.java)
                startActivity(intent)
            }
        })

    }

    private fun checkAllFields(): Boolean {
        if (empNoEt.text.toString().trim().isEmpty()) {
            empNoEt.setError("please enter valid employee no")
            Toast.makeText(this, "please enter valid employee no", Toast.LENGTH_SHORT).show()
            return false
        }
        if (empNameEt.text.toString().trim().isEmpty()) {
            empNameEt.setError("please enter valid employee name")
            Toast.makeText(this, "please enter valid employee name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (empDesignationEt.text.toString().trim().isEmpty()) {
            empDesignationEt.error = "please enter valid designation"
            Toast.makeText(this, "please enter valid designation", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedAccountTypeId <= 0) {
            Toast.makeText(this, "please select valid account type", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedTotalWorkExp <= 0) {
            Toast.makeText(this, "please select your experience", Toast.LENGTH_SHORT).show()
            return false
        }

        // after all validation return true.
        empNoEt.error = null
        empNameEt.error = null
        empDesignationEt.error = null
        return true
    }


}