package com.example.mymusicapp.data.model


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon

data class AudioFile(
    private val id: Long,
    private val title: String,
    private val path: String,
    private val contentUri: Uri?,
    private val thumbnail: Bitmap?,
    private val positionInSongList: Int = AppCommon.INVALID_VALUE,
) {
    constructor() : this(
        -1,
        "NO SONG FOUND",
        "null",
        null,
        BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.library),
    )
    fun getTitle() = title
    fun getId() = id
    fun getPath() = path
    fun getContentUri() = contentUri
    fun getThumbnail() = thumbnail
    fun getPositionInSongList() = positionInSongList

    fun getMediaMetadata(): MediaMetadata {
        if (contentUri != null) {
            return MediaItem.fromUri(contentUri).mediaMetadata
        }
        return MediaMetadata.Builder().build()
    }
}