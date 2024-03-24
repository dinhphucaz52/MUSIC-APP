package com.example.mymusicapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.data.service.MusicService


class SongViewModel : ViewModel() {
    private val dataLiveData = MutableLiveData<ArrayList<AudioFile>>()
    private val positionLiveData = MutableLiveData<Int>()
    private var songList = ArrayList<AudioFile>()
    private fun setData() {
        dataLiveData.postValue(songList)
        positionLiveData.value = MusicService.INVALID_VALUE
    }

    fun getSongList(): LiveData<ArrayList<AudioFile>> {
        return dataLiveData
    }

    fun getPosition(): LiveData<Int> {
        return positionLiveData
    }

    fun init(allAudioFiles: List<AudioFile>) {
        songList = allAudioFiles as ArrayList<AudioFile>
        setData()
    }

    fun updatePosition(position: Int) {
        positionLiveData.postValue(position)
    }
}