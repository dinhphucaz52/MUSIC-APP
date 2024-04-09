package com.example.mymusicapp.presentation.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.data.model.Song
import com.example.mymusicapp.data.repository.MainRepository
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


    private var duration: Long? = null
    private var position: Int = AppCommon.INVALID_VALUE
    private var song: AudioFile = AudioFile()
    private var songList = ArrayList<AudioFile>()
    private val songLiveData = MutableLiveData<AudioFile?>()
    private val songListLiveData = MutableLiveData<ArrayList<AudioFile>>()

    fun setRepository(mainRepository: MainRepository) {
        this.mainRepository = mainRepository
    }

    fun observeSongList(): LiveData<ArrayList<AudioFile>> = songListLiveData
    fun observeSong(): LiveData<AudioFile?> = songLiveData

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            songList = mainRepository.getAllAudioFiles()
            songListLiveData.postValue(songList)
        }
    }

    fun getAudioFile(): AudioFile = song

    fun setSong(position: Int) {
        this.position = position
        song = songList[position]
        songLiveData.postValue(song)
    }

    fun getSongList() = songList

    fun setNextSong() {
        song = songList[++position]
        songLiveData.postValue(song)
    }

    fun setPreviousSong() {
        song = songList[--position]
        songLiveData.postValue(song)
    }

    fun setDuration(duration: Long?) {
        this.duration = duration
    }

    fun getDuration() = duration
}