package com.example.mymusicapp.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.callback.ItemListener
import com.example.mymusicapp.data.model.SongFile
import com.example.mymusicapp.databinding.ActivityPlayListBinding
import com.example.mymusicapp.presentation.adapter.SongAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel

class PlayListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayListBinding
    private val mainMVVM by lazy {
        MainViewModel.getInstance()
    }
    private val songAdapter by lazy {
        SongAdapter(this@PlayListActivity, object : ItemListener {
            override fun onItemClicked(position: Int) {
                TODO("Play song in play list")
            }
        })
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
        mainMVVM.observePlayList().observe(this) { position ->
            val playList = mainMVVM.getPlayList(position)
            songAdapter.updateData(playList.songs as ArrayList<SongFile>)
        }
    }

    private fun init() {
        binding = ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
    }
}