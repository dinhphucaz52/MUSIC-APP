package com.example.mymusicapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.databinding.FragmentHomeBinding
import com.example.mymusicapp.presentation.activity.SongActivity
import com.example.mymusicapp.presentation.adapter.SongAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val mainMVVM = MainViewModel.getInstance()
    private var songAdapter = SongAdapter { position ->
        mainMVVM.setSong(position)
        val intent = Intent(context, SongActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerViews()
        dataBinding()
    }

    private fun dataBinding() {
        mainMVVM.observeSongList().observe(viewLifecycleOwner) {
            println("HomeFragment : $it")
            songAdapter.updateData(it)
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