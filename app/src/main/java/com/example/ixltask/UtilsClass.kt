package com.example.ixltask

import android.content.Context
import android.widget.EditText
import android.widget.Toast

class UtilsClass {
    companion object{
        fun validateEditFields(context: Context, editText: EditText, msg: String): Boolean {
            if (editText.text.toString().trim().isEmpty()) {
                editText.error = msg
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                return false
            } else {
                editText.error = null
                return false
            }
        }

        fun validateSpinnerData(context:Context,selectedSpinnerId:Int,msg:String):Boolean{
            if (selectedSpinnerId==-1){
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                return  false
            }else{
                return  true
            }
        }

    }

}