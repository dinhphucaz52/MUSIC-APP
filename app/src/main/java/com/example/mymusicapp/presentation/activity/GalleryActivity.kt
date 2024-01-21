package com.example.mymusicapp.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusicapp.data.repository.GalleryRepository
import com.example.mymusicapp.databinding.ActivityGalleryBinding
import com.example.mymusicapp.presentation.viewmodel.GalleryViewModel

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private lateinit var galleryRepository: GalleryRepository

    private val galleryMVVM by lazy {
        GalleryViewModel.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
    }

    private fun init() {
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        galleryRepository = GalleryRepository()
        galleryRepository.setContext(this@GalleryActivity)
        galleryMVVM.setRepository(galleryRepository)

    }

    private fun setEvents() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            finish()
        }
    }
}