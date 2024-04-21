package com.example.mymusicapp.data.model

import android.graphics.Bitmap
import com.example.mymusicapp.common.AppCommon


data class Song(
    private val title: String,
    private val thumbnail: Bitmap,
    private val duration: Long,
    private val position: Int = AppCommon.INVALID_VALUE
) {
    fun getName() = title
    fun getThumbnail() = thumbnail
    fun getDuration() = Long
    fun getDurationString(): String {
        var seconds: Long = (duration / 1000)
        val hh = seconds / 3600
        seconds %= 3600
        val mm = seconds / 60
        seconds %= 60
        return "$hh:$mm:$seconds"
    }

    fun getPosition() = position
}
