package com.example.mymusicapp.presentation.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.data.repository.MainRepository
import com.example.mymusicapp.util.MyFactory
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


    private var songList = ArrayList<AudioFile>()
    private val songListLiveData = MutableLiveData<ArrayList<AudioFile>>()

    fun setRepository(mainRepository: MainRepository) {
        this.mainRepository = mainRepository
        loadData()
    }

    fun observeAudioFileList(): LiveData<ArrayList<AudioFile>> = songListLiveData
    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            songList = mainRepository.getAllAudioFiles()
            songListLiveData.postValue(songList)
        }
    }


    private lateinit var controller: MediaController

    fun setController(controller: MediaController) {
        this.controller = controller
    }

    fun getController() = controller
    fun filterSongs(newText: String) {
        val filteredList = ArrayList<AudioFile>()
        for (song in songList) {
            val s = MyFactory.convert(song.getTitle().lowercase())
            val t = MyFactory.convert(newText.lowercase())
            if (s.contains(t))
                filteredList.add(song)
        }
        songListLiveData.value = filteredList
    }


}