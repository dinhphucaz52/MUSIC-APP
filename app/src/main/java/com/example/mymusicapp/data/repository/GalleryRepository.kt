package com.example.mymusicapp.data.repository

import android.content.Context
import com.example.mymusicapp.data.model.ImageFile
import com.example.mymusicapp.helper.FileHelper
import com.example.mymusicapp.helper.StringHelper
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryRepository(val context: Context) {


    private val database by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val storage by lazy {
        FirebaseStorage.getInstance().reference
    }
    fun uploadImage(imageList: ArrayList<ImageFile>) {
        CoroutineScope(Dispatchers.IO).launch {
            imageList.forEach { imageFile ->
                val imageUri = imageFile.uri
                val fileName =
                    FileHelper.getName(context, imageUri)
                storage.child("images").child(fileName).putFile(imageUri)
                    .addOnSuccessListener {
                        println("$fileName uploaded")
                    }
                    .addOnFailureListener {
                        println("$fileName failed with exception ${it.message}")
                    }
                storage.child("images").child(fileName).downloadUrl
                    .addOnSuccessListener { storageUri ->
                        database.child("images")
                            .child(StringHelper.getHash(fileName).toString())
                            .setValue(hashMapOf("name" to fileName, "uri" to storageUri.toString()))
                    }
            }
        }
    }
}