package com.example.mymusicapp.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.databinding.ActivitySelectSongBinding
import com.example.mymusicapp.presentation.adapter.SongPlayListAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel

class SelectSongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectSongBinding
    private val adapter by lazy {
        SongPlayListAdapter(this@SelectSongActivity, MainViewModel.getInstance().getSongList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
    }

    private fun setEvents() {
        binding.apply {
            buttonBack.setOnClickListener {
                finish()
            }
            rvSelectSongList.apply {
                adapter = this@SelectSongActivity.adapter
                layoutManager = LinearLayoutManager(
                    this@SelectSongActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }

    private fun init() {
        binding = ActivitySelectSongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
    }
}