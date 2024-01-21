package com.example.mymusicapp.data.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryRepository {

    private lateinit var context: Context

    private val database by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val storage by lazy {
        FirebaseStorage.getInstance().reference
    }

    fun setContext(context: Context) {
        this.context = context
    }

    private fun getFileExtension(imageUri: Uri): String {
        val contentResolver = context.contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(contentResolver.getType(imageUri))!!
    }

    fun uploadImage(imageUri: Uri): UploadTask {
        val child = System.currentTimeMillis().toString() + "." + getFileExtension(imageUri)
        return storage.child(child).putFile(imageUri)
    }
}