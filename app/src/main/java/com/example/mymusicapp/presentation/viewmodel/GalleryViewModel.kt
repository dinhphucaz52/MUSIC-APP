package com.example.mymusicapp.presentation.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.data.model.ImageFile
import com.example.mymusicapp.data.repository.GalleryRepository


class GalleryViewModel : ViewModel() {

    private lateinit var galleryRepository: GalleryRepository
    fun setRepository(galleryRepository: GalleryRepository) {
        this.galleryRepository = galleryRepository
    }

    companion object {
        @Volatile
        private var instance: GalleryViewModel? = null

        @MainThread
        fun getInstance(): GalleryViewModel {
            return instance ?: synchronized(this) {
                instance ?: GalleryViewModel().also { instance = it }
            }
        }

        @MainThread
        fun clearInstance() {
            instance = null
        }
    }

    fun uploadImages(imageList: ArrayList<ImageFile>) {
        galleryRepository.uploadImage(imageList)
    }

}