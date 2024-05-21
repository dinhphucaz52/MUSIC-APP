package com.example.mymusicapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.callback.ItemListener
import com.example.mymusicapp.data.model.SongFile
import com.example.mymusicapp.databinding.ItemSongPlayListBinding

class SongPlayListAdapter(
    private val context: Context,
    private val callback: ItemListener
) : RecyclerView.Adapter<SongPlayListAdapter.ViewHolder>() {
    private var songList = ArrayList<SongFile>()
    private var isSelectedSong = ArrayList<Boolean>(0)

    inner class ViewHolder(var binding: ItemSongPlayListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                callback.onItemClicked(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSongPlayListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = songList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.binding.apply {
            checkBox.isChecked = isSelectedSong[position]
            checkBox.setOnClickListener {
                isSelectedSong[position] = checkBox.isChecked
            }
            musicNameTxtView.text = song.getTitle()
            if (song.getThumbnail() != null) {
                Glide.with(context)
                    .load(song.getThumbnail())
                    .into(thumbnail)
            }
        }
    }

    fun updateData(songList: ArrayList<SongFile>, selectedSong: ArrayList<Int>) {
        this.songList = songList
        this.isSelectedSong.clear()
        this.isSelectedSong.addAll(List(songList.size) { false })
        selectedSong.forEach {
            isSelectedSong[it] = true
        }
    }

    fun getSelectedSong(): ArrayList<Int> {
        val selectedSong = ArrayList<Int>(0)
        for (i in isSelectedSong.indices) {
            if (isSelectedSong[i]) {
                selectedSong.add(i)
            }
        }
        return selectedSong
    }
}