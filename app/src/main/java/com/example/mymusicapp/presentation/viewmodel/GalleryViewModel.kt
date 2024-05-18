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
        private lateinit var instance: GalleryViewModel
        @MainThread
        fun getInstance(): GalleryViewModel {
            instance = if (::instance.isInitialized) instance else GalleryViewModel()
            return instance
        }
    }

    fun uploadImages(imageList: ArrayList<ImageFile>) {
        galleryRepository.uploadImage(imageList)
    }

}