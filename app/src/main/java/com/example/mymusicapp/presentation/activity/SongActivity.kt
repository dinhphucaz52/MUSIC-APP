package com.example.mymusicapp.presentation.activity

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusicapp.databinding.ActivitySongBinding
import com.example.mymusicapp.presentation.viewmodel.MainViewModel


class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var mainMVVM: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        dataBinding()
        setEvents()
    }

    private fun setEvents() {
        binding.apply {
            btnNext.setOnClickListener {
                mainMVVM.setNextSong()
            }
            btnPrevious.setOnClickListener {
                mainMVVM.setPreviousSong()
            }
        }
    }

    private fun dataBinding() {
        mainMVVM.observeSong().observe(this) {

        }
    }

    private fun init() {
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainMVVM = MainViewModel.getInstance()
    }

}