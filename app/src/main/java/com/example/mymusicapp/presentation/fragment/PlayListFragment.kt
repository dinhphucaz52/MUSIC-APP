package com.example.mymusicapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.callback.DialogListener
import com.example.mymusicapp.callback.ItemListener
import com.example.mymusicapp.data.repository.PlayListRepository
import com.example.mymusicapp.databinding.FragmentPlayListBinding
import com.example.mymusicapp.presentation.activity.PlayListActivity
import com.example.mymusicapp.presentation.adapter.PlayListAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.example.mymusicapp.util.DialogCreatePlayList


class PlayListFragment : Fragment(), DialogListener, ItemListener {

    private lateinit var binding: FragmentPlayListBinding
    private lateinit var playListRepository: PlayListRepository
    private val mainMVVM by lazy {
        MainViewModel.getInstance()
    }

    private val playListAdapter by lazy {
        PlayListAdapter(requireContext(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playListRepository = PlayListRepository(requireContext())
        mainMVVM.setPlayListRepository(playListRepository)
        mainMVVM.loadPlayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayListBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        prepareRecyclerView()
        dataBinding()
        setEvents()
    }

    private fun setEvents() {
        binding.apply {
            fabAddPlaylist.setOnClickListener {
                DialogCreatePlayList.create(requireContext(), this@PlayListFragment)
            }
        }
    }

    private fun dataBinding() {
        mainMVVM.apply {
            observePlayListItem().observe(viewLifecycleOwner) {
                playListAdapter.addPlayList(it)
            }
            observePlayListList().observe(viewLifecycleOwner) {
                playListAdapter.updateData(it)
            }
        }

    }

    private fun prepareRecyclerView() {
        binding.rvPlayList.apply {
            adapter = playListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun init() {

    }

    override fun onDialogClicked(name: String) {
        if (name.isEmpty()) return
        mainMVVM.addPlayList(name)
    }

    override fun onItemClicked(position: Int) {
        mainMVVM.setPlayListPosition(position)
        val intent = Intent(requireContext(), PlayListActivity::class.java)
        startActivity(intent)
    }
}