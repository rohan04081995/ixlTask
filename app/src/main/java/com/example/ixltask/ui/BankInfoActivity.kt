package com.example.ixltask.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.ixltask.BuildConfig
import com.example.ixltask.R
import com.example.ixltask.SqliteHelper
import com.example.ixltask.models.PersonalInfo
import com.example.ixltask.ui.EmployeeInfoActivity.Companion.TAG
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


private const val MULTIPLE_REQUEST_CODE = 1

class BankInfoActivity : AppCompatActivity() {

    private lateinit var bankInfoToolbar: Toolbar
    private lateinit var bankNameEt: EditText
    private lateinit var bankBranchSpinner: Spinner
    private lateinit var accountNoEt: EditText
    private lateinit var ifscCodeEt: EditText
    private lateinit var submitBankInfoButton: Button
    private lateinit var bankProfileIV: ImageView
    private lateinit var bankProfileIButton: ImageButton

    private lateinit var bankBranchesList: List<String>
    private var selectedBankBranchPosition: Int = -MULTIPLE_REQUEST_CODE
    private var bankBranchName: String? = null

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var launch_app_settings: ActivityResultLauncher<Intent>
    private lateinit var multiple_permissions: Array<String>

    private var profilePhotoURI: Uri? = null

    var captured_profile_path: File? = null

    private var cameraLauncher: ActivityResultLauncher<Intent>? = null
    private var myBitmap: Bitmap? = null

    private var personalInfo: PersonalInfo? = null
    private var userId: Int = -1
    private lateinit var sqliteHelper: SqliteHelper

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_info)

        sqliteHelper = SqliteHelper(this)

        bankInfoToolbar = findViewById(R.id.bankInfoToolbar)
        bankNameEt = findViewById(R.id.bankNameEt)
        bankBranchSpinner = findViewById(R.id.bankBranchSpinner)
        accountNoEt = findViewById(R.id.accountNoEt)
        ifscCodeEt = findViewById(R.id.ifscCodeEt)
        submitBankInfoButton = findViewById(R.id.submitBankInfoButton)
        bankProfileIV = findViewById(R.id.bankProfileIV)
        bankProfileIButton = findViewById(R.id.bankProfileIButton)

        bankBranchesList = resources.getStringArray(R.array.backBranchArray).toList()
        multiple_permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)

        var bankBranchAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, bankBranchesList)
        bankBranchSpinner.adapter = bankBranchAdapter
        bankBranchSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedBankBranchPosition = position
                bankBranchName = bankBranchesList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        personalInfo = intent.getParcelableExtra<PersonalInfo>(PersonalInfoActivity.PARCELABLE_KEY)
        userId = intent.getIntExtra(PersonalInfoActivity.USER_ID_KEY, -1)
        Log.d(TAG, "onCreate: bank: personal info: ${personalInfo.toString()}")
        if (userId != -1) {
            bankNameEt.setText(personalInfo!!.bankInfo!!.bankName)
            accountNoEt.setText(personalInfo!!.bankInfo!!.accountNo)
            ifscCodeEt.setText(personalInfo!!.bankInfo!!.ifscCode)

            bankBranchName = personalInfo!!.bankInfo!!.branchName
            selectedBankBranchPosition = bankBranchesList.indexOf(bankBranchName)
            bankBranchSpinner.setSelection(selectedBankBranchPosition)

            myBitmap = sqliteHelper.getUserImage(userId)
            if (myBitmap != null) {
                myBitmap = BitmapFactory.decodeFile(captured_profile_path!!.absolutePath)
                bankProfileIV.setImageBitmap(myBitmap)
                bankProfileIV.setBackgroundResource(0)
                bankProfileIButton.visibility = View.GONE
            }
        }


        bankProfileIV.setOnClickListener {
            Log.d(TAG, "onClick: cam");
            if (checkPermissions()) {
                openCameraForPhoto();
            }

        }
        bankProfileIButton.setOnClickListener(View.OnClickListener {
            if (checkPermissions()) {
                openCameraForPhoto();
            }
        })

        submitBankInfoButton.setOnClickListener {
            Log.d(TAG, "onCreate: bank:myBitmap: $myBitmap")
            if (!checkAllFields()) {
                return@setOnClickListener
            } else {
                personalInfo!!.bankInfo!!.bankName = bankNameEt.text.toString().trim()
                personalInfo!!.bankInfo!!.branchName = bankBranchName!!
                personalInfo!!.bankInfo!!.accountNo = accountNoEt.text.toString().trim()
                personalInfo!!.bankInfo!!.ifscCode = ifscCodeEt.text.toString().trim()

                Log.d(TAG, "onCreate: bank: person info: ${personalInfo.toString()}")

                val gson = Gson()
                val jsonData = gson.toJson(personalInfo)
                Log.d(TAG, "onCreate: bank: jsonData: $jsonData")

                if (userId != -1) {
                    sqliteHelper.updateUserData(jsonData, userId, myBitmap!!)
                } else {
                    sqliteHelper.addUserData(jsonData, myBitmap!!)
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                val cam: Boolean = it[Manifest.permission.CAMERA] == false
                val store: Boolean = it[Manifest.permission.READ_MEDIA_IMAGES] == false
                Log.d(
                    TAG,
                    "onActivityResult: camera perms result: " + it.toString() + " :cam: " + cam + " :store: " + "store"
                )

                if (cam && store) {
                    Log.d(TAG, "onActivityResult: open camera")
                    openCameraForPhoto()
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.CAMERA
                        ) || !ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.READ_MEDIA_IMAGES
                        )
                    ) {
                        showPermsDenied_dialog("This application requires camera and storage permission to work properly, do you want to enable it?")
                    }
                }
            }
        launch_app_settings =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback<ActivityResult> { result ->
                    Log.d(
                        TAG,
                        "onActivityResult: settings " + result.resultCode + " : " + result.toString()
                    )
                })

        cameraLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.d(TAG, "onActivityResult: camera ActivityResult: $result")
            if (result.resultCode == RESULT_OK) {
                if (captured_profile_path!!.exists()) {
//                    getImageFromCameraUri(profilePhotoURI)
                    myBitmap = BitmapFactory.decodeFile(captured_profile_path!!.absolutePath)

                    myBitmap = getResizedBitmap(myBitmap!!, 480)

                    bankProfileIV.setImageBitmap(myBitmap)
                    bankProfileIV.setBackgroundResource(0)
                    bankProfileIButton.visibility = View.GONE
                }
            }
        }
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun openCameraForPhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val appFolderPath = createFolder()

        captured_profile_path = File(appFolderPath, "ixl_${timeStamp}.jpg")
        profilePhotoURI = FileProvider.getUriForFile(
            this, BuildConfig.APPLICATION_ID + ".provider", captured_profile_path!!
        )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, profilePhotoURI)
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        cameraLauncher!!.launch(cameraIntent)

    }

    fun createFolder(): File {
        val folder: File? = getExternalFilesDir("ixl")
        if (folder!!.exists()) {
            folder.mkdir()
        }
        Log.d(TAG, "createFolder:profile:folder: $folder")
        return folder
    }


    private fun checkAllFields(): Boolean {
        if (bankNameEt.text.toString().trim().isEmpty()) {
            bankNameEt.setError("please enter valid bank name")
            Toast.makeText(this, "please enter valid bank name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedBankBranchPosition <= 0) {
            Toast.makeText(this, "please select valid bank branch", Toast.LENGTH_SHORT).show()
            return false
        }
        if (accountNoEt.text.toString().trim().isEmpty()) {
            accountNoEt.setError("please enter valid account number")
            Toast.makeText(this, "please enter valid account number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (ifscCodeEt.text.toString().trim().isEmpty()) {
            ifscCodeEt.error = "please enter valid ifsc code"
            Toast.makeText(this, "please enter valid ifsc code", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedBankBranchPosition <= 0) {
            Toast.makeText(this, "please select valid bank branch", Toast.LENGTH_SHORT).show()
            return false
        }
        if (myBitmap == null) {
            Toast.makeText(this, "please capture your image", Toast.LENGTH_SHORT).show()
            return false
        }


        // after all validation return true.
        bankNameEt.error = null
        accountNoEt.error = null
        ifscCodeEt.error = null
        return true
    }

    fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        launch_app_settings.launch(intent)
    }

    private fun showPermsDenied_dialog(message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                openSettings()
            }).setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun checkPermissions(): Boolean {
        Log.d(TAG, "checkPermissions: ")
        val permissionsNeeded: MutableList<String> = ArrayList()
        for (perms in multiple_permissions) {
            if (ContextCompat.checkSelfPermission(
                    this, perms
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNeeded.add(perms)
            }
        }
        if (!permissionsNeeded.isEmpty()) {

            Log.d(TAG, "checkPermissions: ${permissionsNeeded.toString()}")
//            cameraPermissionLauncher.launch(permissionsNeeded.toTypedArray())
            ActivityCompat.requestPermissions(
                this, permissionsNeeded.toTypedArray(), MULTIPLE_REQUEST_CODE
            )
            return false
        }
        return true
    }
}