package com.example.mymusicapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

object ThumbnailFactory {
    fun getMp3Thumbnail(filePath: String): Bitmap? {

        val retriever = MediaMetadataRetriever()

        return try {
            retriever.setDataSource(filePath)
            val embeddedThumbnail = retriever.embeddedPicture

            if (embeddedThumbnail != null) {
                BitmapFactory.decodeByteArray(embeddedThumbnail, 0, embeddedThumbnail.size)
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error extracting thumbnail from MP3: ${e.message}")
            null
        } finally {
            retriever.release()
        }
    }
}