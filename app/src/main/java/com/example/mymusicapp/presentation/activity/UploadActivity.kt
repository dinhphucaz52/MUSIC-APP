package com.example.mymusicapp.presentation.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mymusicapp.databinding.ActivityUploadBinding
import com.example.mymusicapp.presentation.viewmodel.GalleryViewModel

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val galleryRepository by lazy {
        galleryMVVM.getGalleryRepository()
    }
    private val galleryMVVM by lazy {
        GalleryViewModel.getInstance()
    }

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
        dataBinding()
    }

    private fun dataBinding() {

    }

    private fun setEvents() {
        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data?.data
                if (!isDestroyed) {
                    Glide.with(this).load(imageUri).into(binding.buttonLoadImage)
                }
            } else {
                Toast.makeText(this@UploadActivity, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.apply {
            buttonLoadImage.setOnClickListener {
                val photoPickerIntent = Intent()
                photoPickerIntent.type = "image/*"
                photoPickerIntent.action = Intent.ACTION_GET_CONTENT
                activityResultLauncher.launch(photoPickerIntent)
            }

            buttonUpload.setOnClickListener {
                if (imageUri != null) {
                    galleryRepository.uploadImage(imageUri!!).apply {
                        addOnSuccessListener {
                            Toast.makeText(this@UploadActivity, "Uploaded", Toast.LENGTH_SHORT)
                                .show()
                        }
                        addOnProgressListener {
                            val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                            binding.progressBar.progress = progress.toInt()
                        }
                        addOnFailureListener {
                            Toast.makeText(
                                this@UploadActivity,
                                "Upload failed with error: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@UploadActivity, "Please select an image", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun init() {
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
    }
}
