package com.example.mymusicapp.data.model


import android.graphics.Bitmap
import android.net.Uri

data class AudioFile(
    private val id: Long,
    private val title: String,
    private val path: String,
    private val contentUri: Uri?,
    private val thumbnail: Bitmap?
) {
    fun getTitle() = title
    fun getId() = id
    fun getPath() = path
    fun getContentUri() = contentUri
    fun getThumbnail() = thumbnail
}