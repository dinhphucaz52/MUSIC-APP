package com.example.mymusicapp.data.model

import android.graphics.Bitmap


data class Song(
    private val id: Long,
    private val title: String,
    private val path: String,
    private val thumbnail: Bitmap,
    private val duration: Long
) {
    fun getName() = title
    fun getThumbnail() = thumbnail
    fun getDuration() = Long
}
