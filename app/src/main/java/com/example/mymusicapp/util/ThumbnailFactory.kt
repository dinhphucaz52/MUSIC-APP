package com.example.mymusicapp.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.example.mymusicapp.R

object ThumbnailFactory {
     fun getMp3Thumbnail(filePath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(filePath)
            val embeddedThumbnail = retriever.embeddedPicture

            return if (embeddedThumbnail != null) {
                BitmapFactory.decodeByteArray(embeddedThumbnail, 0, embeddedThumbnail.size)
            } else {
                BitmapFactory.decodeResource(
                    Resources.getSystem(),
                    R.drawable.item_ic_song
                )
            }
        } catch (e: Exception) {
            println("Error extracting thumbnail from MP3: ${e.message}")
            return null
        } finally {
            retriever.release()
        }
    }
}