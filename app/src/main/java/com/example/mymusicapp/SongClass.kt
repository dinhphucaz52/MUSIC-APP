package com.example.mymusicapp

import android.media.MediaPlayer
import android.os.Parcel
import android.os.Parcelable
import java.time.Duration

class SongClass(
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

    companion object CREATOR : Parcelable.Creator<SongClass> {
        override fun createFromParcel(parcel: Parcel): SongClass {
            return SongClass(parcel)
        }

        override fun newArray(size: Int): Array<SongClass?> {
            return arrayOfNulls(size)
        }
    }
}