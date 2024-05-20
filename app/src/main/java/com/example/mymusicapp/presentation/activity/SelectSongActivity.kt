package com.example.mymusicapp.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.callback.ItemListener
import com.example.mymusicapp.databinding.ActivitySelectSongBinding
import com.example.mymusicapp.presentation.adapter.SongPlayListAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel

class SelectSongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectSongBinding
    private val mainMVVM by lazy {
        MainViewModel.getInstance()
    }

    private val selectedPosition by lazy {
        mutableListOf<Int>()
    }

    private val adapter by lazy {
        SongPlayListAdapter(
            this@SelectSongActivity,
            MainViewModel.getInstance().getSongList(),
            object : ItemListener {
                override fun onItemClicked(position: Int) {
                    if (position < 0)
                        selectedPosition.remove(position)
                    else
                        selectedPosition.add(position)
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
    }

    private fun setEvents() {
        binding.apply {
            buttonBack.setOnClickListener {
                mainMVVM.updateSongList(selectedPosition)
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