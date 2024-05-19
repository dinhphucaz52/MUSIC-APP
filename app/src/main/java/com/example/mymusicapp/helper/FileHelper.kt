package com.example.mymusicapp.helper

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object FileHelper {
     fun getExtension(context: Context, imageUri: Uri): String {
        val contentResolver = context.contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(contentResolver.getType(imageUri))!!
    }
     fun getName(context: Context, imageUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        var displayName = "unnamed"

        val cursor: Cursor? = context.contentResolver.query(
            imageUri,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex =
                    it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                displayName = it.getString(displayNameIndex)
            }
        }
        return displayName
    }
}