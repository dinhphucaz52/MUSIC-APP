package com.example.mymusicapp.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.ByteArrayOutputStream

object BitmapHelper {
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

    fun getBitmap(context: Context, imageUri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        val scaledBitmap = scaleBitmap(bitmap, 0.3f)
        return scaledBitmap
    }

    private fun scaleBitmap(bitmap: Bitmap, scale: Float): Bitmap {
        val width = (bitmap.width * scale).toInt()
        val height = (bitmap.height * scale).toInt()

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

        val byteArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}