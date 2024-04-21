package com.example.mymusicapp.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.example.mymusicapp.R

object ThumbnailFactory {
    fun getMp3Thumbnail(filePath: String): Bitmap {

        val tempBitmap = BitmapFactory.decodeResource(
            Resources.getSystem(), R.drawable.item_ic_song
        )

        val retriever = MediaMetadataRetriever()

        return try {
            retriever.setDataSource(filePath)
            val embeddedThumbnail = retriever.embeddedPicture

            if (embeddedThumbnail != null) {
                BitmapFactory.decodeByteArray(embeddedThumbnail, 0, embeddedThumbnail.size)
            } else {
                tempBitmap
            }
        } catch (e: Exception) {
            println("Error extracting thumbnail from MP3: ${e.message}")
            tempBitmap
        } finally {
            retriever.release()
        }
    }
}