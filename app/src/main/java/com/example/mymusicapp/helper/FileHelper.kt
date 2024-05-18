package com.example.mymusicapp.helper

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

object FileHelper {
     fun getExtension(context: Context, imageUri: Uri): String {
        val contentResolver = context.contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(contentResolver.getType(imageUri))!!
    }
}