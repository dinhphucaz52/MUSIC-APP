package com.example.mymusicapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mymusicapp.callback.DialogListener
import com.example.mymusicapp.callback.ItemListener
import com.example.mymusicapp.data.repository.PlayListRepository
import com.example.mymusicapp.databinding.FragmentPlayListBinding
import com.example.mymusicapp.presentation.activity.PlayListActivity
import com.example.mymusicapp.presentation.adapter.PlayListAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.example.mymusicapp.util.DialogCreatePlayList


class PlayListFragment : Fragment() {

    private lateinit var binding: FragmentPlayListBinding
    private lateinit var playListRepository: PlayListRepository
    private val mainMVVM by lazy {
        MainViewModel.getInstance()
    }

    private val playListAdapter by lazy {
        PlayListAdapter(requireContext(), @UnstableApi object : ItemListener {
            override fun onItemClicked(position: Int) {
                mainMVVM.setPlayList(position)
                binding.dialogBottom.visibility = View.VISIBLE
                val intent = Intent(requireContext(), PlayListActivity::class.java)
                startActivity(intent)
            }
        })
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
                DialogCreatePlayList.create(requireContext(), object : DialogListener {
                    override fun onDialogClicked(name: String) {
                        if (name.isNotEmpty())
                            mainMVVM.addPlayList(name)
                    }
                })
            }
            buttonDelete.setOnClickListener {
                mainMVVM.deletePlayList()
            }
        }
    }

    private fun dataBinding() {
        mainMVVM.apply {
            observePlayListList().observe(viewLifecycleOwner) {
                playListAdapter.updateData(it)
            }
            observePlayList().observe(viewLifecycleOwner) {
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

    private fun prepareRecyclerView() {
        binding.rvPlayList.apply {
            adapter = playListAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun init() {

    }
}