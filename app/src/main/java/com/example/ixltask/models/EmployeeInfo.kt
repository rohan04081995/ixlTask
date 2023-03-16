package com.example.ixltask.models

import android.os.Parcel
import android.os.Parcelable

data class EmployeeInfo(
    var empNo: String = "",
    var empName: String = "",
    var empDesignation: String = "",
    var accountType: String = "",
    var workExp: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(empNo)
        parcel.writeString(empName)
        parcel.writeString(empDesignation)
        parcel.writeString(accountType)
        parcel.writeString(workExp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmployeeInfo> {
        override fun createFromParcel(parcel: Parcel): EmployeeInfo {
            return EmployeeInfo(parcel)
        }

        override fun newArray(size: Int): Array<EmployeeInfo?> {
            return arrayOfNulls(size)
        }
    }
}
