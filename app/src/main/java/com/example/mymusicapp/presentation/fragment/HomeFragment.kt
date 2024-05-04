package com.example.mymusicapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.media3.session.MediaController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.databinding.FragmentHomeBinding
import com.example.mymusicapp.presentation.activity.SongActivity
import com.example.mymusicapp.presentation.adapter.SongAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val mainMVVM = MainViewModel.getInstance()

    private var songAdapter = SongAdapter { position ->
        mainMVVM.getController().seekTo(position, 0)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("HomeFragment: onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerViews()
        dataBinding()
        addEvents()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }


    private fun setData() {
    }


    private fun addEvents() {
        binding.apply {
            searchEditText.apply {
                clearFocus()
                setOnQueryTextListener(object: OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        mainMVVM.filterSongs(newText ?: "")
                        return true
                    }

                })
            }
            btnNextSong.setOnClickListener {
                startActivity(Intent(activity, SongActivity::class.java))
            }
        }
    }

    private fun dataBinding() {
        mainMVVM.observeAudioFileList().observe(viewLifecycleOwner) {
            songAdapter.updateData(it)
        }
        mainMVVM.observeSongName().observe(viewLifecycleOwner) {
            binding.tvSongName.text = it
        }
    }

    private fun prepareRecyclerViews() {
        binding.songRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = songAdapter
        }
    }
}