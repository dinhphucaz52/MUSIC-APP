package com.example.mymusicapp.presentation.viewmodel

import android.provider.MediaStore.Audio
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.data.repository.MainRepository
import com.example.mymusicapp.data.service.MusicService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private lateinit var mainRepository: MainRepository

    companion object {
        private lateinit var instance: MainViewModel

        @MainThread
        fun getInstance(): MainViewModel {
            instance = if (::instance.isInitialized) instance else MainViewModel()
            return instance
        }
    }

    private val song: AudioFile? = null
    private val songListLiveData = MutableLiveData<ArrayList<AudioFile>>()
    private val songLiveData = MutableLiveData<AudioFile?>()

    fun setRepository(mainRepository: MainRepository) {
        this.mainRepository = mainRepository
    }

    fun observeSongList(): LiveData<ArrayList<AudioFile>> = songListLiveData
    fun observeSong(): LiveData<AudioFile?> = songLiveData

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            songListLiveData.postValue(mainRepository.getAllAudioFiles())
        }
    }

    fun getSong() = song
}