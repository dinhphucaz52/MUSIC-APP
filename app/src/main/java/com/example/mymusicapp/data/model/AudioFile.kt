package com.example.mymusicapp.data.model


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.mymusicapp.R

data class AudioFile(
    private val id: Long,
    private val title: String,
    private val path: String,
    private val contentUri: Uri?,
    private val thumbnail: Bitmap?
) {
    constructor() : this(
        -1,
        "NO SONG FOUND",
        "null",
        null,
        BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.library)
    )

    fun getTitle() = title
    fun getId() = id
    fun getPath() = path
    fun getContentUri() = contentUri
    fun getThumbnail() = thumbnail
}