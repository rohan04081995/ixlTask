package com.example.ixltask.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ixltask.R
import com.example.ixltask.SqliteHelper
import com.example.ixltask.UserDataAdapter
import com.example.ixltask.models.PersonalInfo
import com.example.ixltask.models.UserData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), UserDataAdapter.UserDataAdapterClickListener {
    private lateinit var mainToolbar: Toolbar
    private lateinit var mainRv: RecyclerView
    private lateinit var noDataTv: TextView
    private lateinit var addUserFab: FloatingActionButton

    private lateinit var sqliteHelper: SqliteHelper
    private lateinit var userDataList: List<UserData>
    private lateinit var personalInfoList: MutableList<PersonalInfo>

    private lateinit var adapter: UserDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sqliteHelper = SqliteHelper(this)
        userDataList = ArrayList()
        personalInfoList = mutableListOf()

        mainToolbar = findViewById(R.id.mainToolbar)
        mainRv = findViewById(R.id.mainRv)
        noDataTv = findViewById(R.id.noDataTv)
        addUserFab = findViewById(R.id.addUserFab)

        userDataList = sqliteHelper.getAllUsers()

        val gson = Gson()

        for (element in userDataList) {
            val personalData = gson.fromJson(element.user_data, PersonalInfo::class.java)

            personalInfoList.add(personalData)
        }
        adapter = UserDataAdapter(personalInfoList, this)
        mainRv.adapter = adapter
        mainRv.layoutManager = LinearLayoutManager(this)

        if (personalInfoList.isEmpty()) {
            noDataTv.visibility = View.VISIBLE
            mainRv.visibility = View.GONE
        } else {
            mainRv.visibility = View.VISIBLE
            noDataTv.visibility = View.GONE
        }

        addUserFab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PersonalInfoActivity::class.java)
            startActivity(intent)
        })

    }

    override fun userDataAdapterClick(position: Int) {
        val intent = Intent(this, PersonalInfoActivity::class.java)
        intent.putExtra(PersonalInfoActivity.USER_ID_KEY, userDataList[position].id)
        intent.putExtra(PersonalInfoActivity.PARCELABLE_KEY, personalInfoList[position])
        startActivity(intent)
    }
}