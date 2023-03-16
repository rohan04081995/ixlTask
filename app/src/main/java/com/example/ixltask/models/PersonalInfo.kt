package com.example.ixltask.models

import android.os.Parcel
import android.os.Parcelable

data class PersonalInfo(
    var firstName: String="",
    var lastName: String="",
    var phoneName: String="",
    var gender: Int=0,
    var dob: String="",
    var employeeInfo: EmployeeInfo?=null,
    var bankInfo: BankInfo?=null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readParcelable(EmployeeInfo::class.java.classLoader),
        parcel.readParcelable(BankInfo::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(phoneName)
        parcel.writeInt(gender)
        parcel.writeString(dob)
        parcel.writeParcelable(employeeInfo, flags)
        parcel.writeParcelable(bankInfo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonalInfo> {
        override fun createFromParcel(parcel: Parcel): PersonalInfo {
            return PersonalInfo(parcel)
        }

        override fun newArray(size: Int): Array<PersonalInfo?> {
            return arrayOfNulls(size)
        }
    }
}
