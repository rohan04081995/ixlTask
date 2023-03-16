package com.example.ixltask.models

import android.os.Parcel
import android.os.Parcelable

data class BankInfo(
    var bankName: String = "",
    var branchName: String = "",
    var accountNo: String = "",
    var ifscCode: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bankName)
        parcel.writeString(branchName)
        parcel.writeString(accountNo)
        parcel.writeString(ifscCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BankInfo> {
        override fun createFromParcel(parcel: Parcel): BankInfo {
            return BankInfo(parcel)
        }

        override fun newArray(size: Int): Array<BankInfo?> {
            return arrayOfNulls(size)
        }
    }
}
