package com.example.mymusicapp.repository.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.data.model.SongClass
import com.example.mymusicapp.repository.adapter.SongAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    /*--------------------------------------------*/
    private val songList = ArrayList<SongClass>()
    private val songAdapter = SongAdapter(songList) {_ ->
    }

    private lateinit var songRecyclerView: RecyclerView

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
        addSong()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songRecyclerView = view.findViewById(R.id.songRecyclerView)
        songRecyclerView.layoutManager = LinearLayoutManager(context)
        songRecyclerView.adapter = songAdapter
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

    private fun addSong() {
        songList.add(SongClass("Nếu ta ngược lối", R.raw.neu_ta_nguoc_loi))
        songList.add(SongClass("Không lấy được vợ", R.raw.khong_lay_duoc_vo))
        songList.add(SongClass("Nước mắt chia đôi", R.raw.nuoc_mat_chia_doi))
        songList.add(SongClass("Thôi ngừng nhớ em", R.raw.thoi_ngung_nho_em))
        songList.add(SongClass("Xoá hết yêu thương", R.raw.xoa_het_yeu_thuong))
        songList.add(SongClass("Yêu em là định mệnh", R.raw.yeu_em_la_dinh_menh))
        songList.add(SongClass("Anh không biết bao lâu", R.raw.anh_khong_biet_bao_lau))
        songList.add(SongClass("Người ấy sẽ quên em thôi", R.raw.nguoi_ay_se_quen_em_thoi))
        songList.add(SongClass("Anh quên mình đã chia tay", R.raw.anh_quen_minh_da_chia_tay))
        songList.sortBy {
            it.title
        }
    }


}