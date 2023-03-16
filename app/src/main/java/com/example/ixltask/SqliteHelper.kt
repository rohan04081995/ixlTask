package com.example.ixltask

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.ixltask.models.UserData
import java.io.ByteArrayOutputStream


class SqliteHelper(
    context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "ixl_db"
        private val DATABASE_VERSION = 2
        val TABLE_NAME = "ixl_table"

        val ID_COL = "id"
        val USER_DATA_COl = "user_data"
        val USER_IMAGE_COL = "userImage"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            ("CREATE TABLE " + TABLE_NAME + " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USER_DATA_COl + " TEXT "
//                    + USER_IMAGE_COL + " BLOB "
                    + ")")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addUserData(jsonData: String?, myBitmap: Bitmap) {
        val db = writableDatabase
        val contentValues = ContentValues()

        /*val stream = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()*/

        contentValues.put(USER_DATA_COl, jsonData)
//        contentValues.put(USER_IMAGE_COL, byteArray)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }


    fun updateUserData(jsonData: String?, userId: Int, myBitmap: Bitmap) {
        /*val stream = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()*/

        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_DATA_COl, jsonData)
//        contentValues.put(USER_IMAGE_COL, byteArray)
        db.update(TABLE_NAME, contentValues, "id=$userId", null)
        db.close()
    }

    @SuppressLint("Range")
    fun getUserImage(userId: Int): Bitmap? {
        val db = readableDatabase
        val query = "Select $USER_IMAGE_COL From $TABLE_NAME Where $ID_COL = $userId"
        val cursor = db.rawQuery(query, null)

        val imgByte: ByteArray = cursor.getBlob(cursor.getColumnIndex(USER_IMAGE_COL))
        db.close()
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size);

    }

    @SuppressLint("Range")
    fun getAllUsers(): List<UserData> {
        val userDataList = ArrayList<UserData>()

        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var userJsonData: String

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex(ID_COL))
                userJsonData = cursor.getString(cursor.getColumnIndex(USER_DATA_COl))
                val user = UserData(id = userId, user_data = userJsonData)
                userDataList.add(user)
            } while (cursor.moveToNext())
        }
        return userDataList
    }
}