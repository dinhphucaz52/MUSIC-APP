package com.example.mymusicapp.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.databinding.ActivityPlayListBinding
import com.example.mymusicapp.presentation.adapter.SongAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel

class PlayListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayListBinding
    private val mainMVVM by lazy {
        MainViewModel.getInstance()
    }
    private val songAdapter by lazy {
        SongAdapter(this@PlayListActivity) {
            onItemClick(it)
        }
    }

    private fun onItemClick(position: Int) {
        Toast.makeText(this@PlayListActivity, "$position", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
        prepareRecyclerView()
        dataBinding()
    }

    private fun prepareRecyclerView() {
        binding.rvPlayList.apply {
            adapter = songAdapter
            layoutManager =
                LinearLayoutManager(this@PlayListActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setEvents() {
        binding.apply {
            buttonBack.setOnClickListener {
                finish()
            }
            fab.setOnClickListener {
                startActivity(Intent(this@PlayListActivity, SelectSongActivity::class.java))
            }
        }
    }

    private fun dataBinding() {
        mainMVVM.observePlayListPosition().observe(this) { position ->
            val songList = mainMVVM.getSongList(position)
            binding.apply {
                tvPlayListName.text = songList.name
                songAdapter.updateData(mainMVVM.getSongList())
            }
        }
    }

    private fun init() {
        binding = ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
    }
}