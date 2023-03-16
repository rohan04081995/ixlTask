package com.example.ixltask.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.ixltask.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
private  lateinit var mainToolbar:Toolbar
    private lateinit var  mainRv:RecyclerView
    private lateinit var  noDataTv:TextView
   private lateinit var  addUserFab:FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainToolbar=findViewById(R.id.mainToolbar)
        mainRv=findViewById(R.id.mainRv)
        noDataTv=findViewById(R.id.noDataTv)
        addUserFab=findViewById(R.id.addUserFab)


        addUserFab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,PersonalInfoActivity::class.java)
            startActivity(intent)
        })

    }
}