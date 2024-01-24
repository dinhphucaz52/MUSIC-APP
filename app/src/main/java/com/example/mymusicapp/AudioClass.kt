package com.example.mymusicapp

import android.os.Parcel
import android.os.Parcelable

class AudioClass(
    val title: String?,
    val music: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(music)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioClass> {
        override fun createFromParcel(parcel: Parcel): AudioClass {
            return AudioClass(parcel)
        }

        override fun newArray(size: Int): Array<AudioClass?> {
            return arrayOfNulls(size)
        }
    }
}