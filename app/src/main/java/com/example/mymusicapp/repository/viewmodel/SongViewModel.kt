package com.example.mymusicapp.repository.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.R
import com.example.mymusicapp.data.model.SongClass
import com.example.mymusicapp.data.service.MusicService


class SongViewModel : ViewModel() {
    private val dataLiveData = MutableLiveData<ArrayList<SongClass>>()
    private val positionLiveData = MutableLiveData<Int>()
    val songList = ArrayList<SongClass>()
    private fun setData() {
        dataLiveData.value = songList
        positionLiveData.value = MusicService.INVALID_VALUE
    }

    fun getSongList(): LiveData<ArrayList<SongClass>> {
        return dataLiveData
    }

    fun getPosition(): LiveData<Int> {
        return positionLiveData
    }

    fun init() {
        songList.add(SongClass("Nếu ta ngược lối", R.raw.neu_ta_nguoc_loi))
        songList.add(SongClass("Không lấy được vợ", R.raw.khong_lay_duoc_vo))
        songList.add(SongClass("Nước mắt chia đôi", R.raw.nuoc_mat_chia_doi))
        songList.add(SongClass("Thôi ngừng nhớ em", R.raw.thoi_ngung_nho_em))
        songList.add(SongClass("Xoá hết yêu thương", R.raw.xoa_het_yeu_thuong))
        songList.add(SongClass("Yêu em là định mệnh", R.raw.yeu_em_la_dinh_menh))
        songList.add(SongClass("Anh không biết bao lâu", R.raw.anh_khong_biet_bao_lau))
        songList.add(SongClass("Người ấy sẽ quên em thôi", R.raw.nguoi_ay_se_quen_em_thoi))
        songList.add(SongClass("Anh quên mình đã chia tay", R.raw.anh_quen_minh_da_chia_tay))
        songList.sortBy {
            it.title
        }
        setData()
    }

    fun updatePosition(position: Int) {
        positionLiveData.postValue(position)
    }
}