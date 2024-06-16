package com.example.mymusicapp.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mymusicapp.R
import com.example.mymusicapp.callback.ItemListener
import com.example.mymusicapp.databinding.ActivityMyPlayListBinding
import com.example.mymusicapp.presentation.adapter.PlayListAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel

class MyPlayListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPlayListBinding
    private lateinit var mainMVVM: MainViewModel
    private lateinit var playListAdapter: PlayListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        prepareRecyclerView()
        observeData()
        setEvents()
    }

    private fun prepareRecyclerView() {
        binding.rvPlayList.apply {
            adapter = playListAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun init() {
        binding = ActivityMyPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        mainMVVM = MainViewModel.getInstance()
        playListAdapter =
            PlayListAdapter(this, @UnstableApi object : ItemListener {
                override fun onItemClicked(position: Int) {
                    mainMVVM.setPlayList(position)
                    binding.dialogBottom.visibility = View.VISIBLE
                    val intent = Intent(this@MyPlayListActivity, PlayListActivity::class.java)
                    startActivity(intent)
                }
            })
        mainMVVM.loadPlayListFromFirebase()
    }

    private fun setEvents() {

    }

    private fun observeData() {
        mainMVVM.apply {
            observePlayListList().observe(this@MyPlayListActivity) {
                playListAdapter.updateData(it)
            }
            observePlayList().observe(this@MyPlayListActivity) {
                val playList = mainMVVM.getPlayList()
                if (playList != null) {
                    binding.apply {
                        textView5.text = playList.name
                        textView6.text = playList.songs.size.toString()
                    }
                } else {
                    binding.dialogBottom.visibility = View.GONE
                }
            }
        }
    }
}