package com.example.mymusicapp.repository.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.data.model.SongClass
import com.example.mymusicapp.repository.adapter.SongAdapter
import com.example.mymusicapp.repository.viewmodel.SongViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    /*--------------------------------------------*/

    private val shareSongViewModel by lazy {
        ViewModelProvider(requireActivity())[SongViewModel::class.java]
    }

    private var audioList = ArrayList<SongClass>()
    private lateinit var songRecyclerView: RecyclerView


    private var songAdapter = SongAdapter(audioList) { position ->
        shareSongViewModel.updatePosition(position)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songRecyclerView = view.findViewById(R.id.songRecyclerView)
        songRecyclerView.layoutManager = LinearLayoutManager(context)
        songRecyclerView.adapter = songAdapter

        shareSongViewModel.getSongList().observe(viewLifecycleOwner) { songList ->
            songAdapter.updateData(songList)
        }

        shareSongViewModel.getPosition().observe(viewLifecycleOwner) { position ->
//            songAdapter.notifyItemChanged(position)
            println("HomeFragment: $position")
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}