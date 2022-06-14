package com.lada.vicinity.utils

import android.os.Parcel
import android.os.Parcelable

data class Model(
    var name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.name)
    }

    companion object CREATOR : Parcelable.Creator<Model> {
        override fun createFromParcel(parcel: Parcel): Model {
            return Model(parcel)
        }

        override fun newArray(size: Int): Array<Model?> {
            return arrayOfNulls(size)
        }
    }

}