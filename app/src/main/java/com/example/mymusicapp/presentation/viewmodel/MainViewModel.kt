package com.example.mymusicapp.presentation.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.model.PlayList
import com.example.mymusicapp.data.model.SongFile
import com.example.mymusicapp.data.repository.MainRepository
import com.example.mymusicapp.data.repository.PlayListRepository
import com.example.mymusicapp.helper.StringHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private lateinit var mainRepository: MainRepository
    private lateinit var playListRepository: PlayListRepository
    fun setRepository(mainRepository: MainRepository) {
        this.mainRepository = mainRepository
    }
    fun setPlayListRepository(playListRepository: PlayListRepository) {
        this.playListRepository = playListRepository
    }
    companion object {
        private lateinit var instance: MainViewModel

        @MainThread
        fun getInstance(): MainViewModel {
            instance = if (::instance.isInitialized) instance else MainViewModel()
            return instance
        }
    }

    fun getSongList() = songList

    private var songList = ArrayList<SongFile>()
    private val songListLiveData = MutableLiveData<ArrayList<SongFile>>()
    fun observeSongsList(): LiveData<ArrayList<SongFile>> = songListLiveData
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            songList = mainRepository.getAllAudioFiles()
            songListLiveData.postValue(songList)
        }
    }

    fun loadData(playListPosition: Int) {
        val songListPrepareUpdate = if (playListPosition == AppCommon.LOCAL_FILES)
            songList
        else
            playListList[playListPosition].songs as ArrayList<SongFile>
        if (songListPrepareUpdate != songListLiveData.value)
            songListLiveData.postValue(songListPrepareUpdate)
    }
    private lateinit var controller: MediaController
    fun setController(controller: MediaController) {
        this.controller = controller
    }
    fun getController() = controller
    fun filterSongs(newText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredList = ArrayList<SongFile>()
            for (song in songList) {
                val s = StringHelper.convert(song.getTitle().lowercase())
                val t = StringHelper.convert(newText.lowercase())
                if (s.contains(t))
                    filteredList.add(song)
            }
            songListLiveData.postValue(filteredList)
        }
    }
    private val songNameLiveData = MutableLiveData("NO SONG FOUND")
    fun observeSongName(): LiveData<String> = songNameLiveData
    fun setSongName(songName: String) {
        songNameLiveData.postValue(songName)
    }

    //PlayList
    private val playListList = ArrayList<PlayList>()
    private val playListListLiveData = MutableLiveData<ArrayList<PlayList>>()
    fun observePlayListList(): LiveData<ArrayList<PlayList>> = playListListLiveData

    fun loadPlayList() {
        CoroutineScope(Dispatchers.IO).launch {
            val songListEntity = playListRepository.getSongs()
            val playListEntity = playListRepository.getPlayLists()
            val playListHashMap = hashMapOf<Int, PlayList>()
            playListEntity.forEach {
                playListHashMap[it.id] = PlayList(it.id, it.name, mutableListOf())
            }
            songListEntity.forEach {
                playListHashMap[it.playListId]!!.songs.add(
                    songList.find { song ->
                        song.getContentUri().toString() == it.contentURI
                    }!!
                )
            }
            playListList.clear()
            playListHashMap.forEach {
                playListList.add(it.value)
            }
            CoroutineScope(Dispatchers.Main).launch {
                playListListLiveData.postValue(playListList)
            }
        }
    }

    private val playListPositionLiveData = MutableLiveData<Int>()
    fun observePlayList(): LiveData<Int> = playListPositionLiveData
    fun setPlayList(position: Int) {
        playListPositionLiveData.postValue(position)
    }

    fun addPlayList(name: String) {
        playListRepository.addPlayList(name)
    }

    fun updateSongList(selectedPosition: MutableList<Int>) {
        selectedPosition.sort()
        val position = playListPositionLiveData.value!!
        val playList = playListList[position]
        playList.songs.clear()
        selectedPosition.forEach {
            playList.songs.add(songList[it])
        }
        playListPositionLiveData.postValue(position)
        playListRepository.updatePlayList(playList)
    }

    fun getPlayList(position: Int): PlayList = playListList[position]
    fun getPlayList() = playListList[playListPositionLiveData.value!!]


}