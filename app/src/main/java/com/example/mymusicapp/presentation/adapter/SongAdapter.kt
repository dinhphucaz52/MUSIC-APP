package com.example.mymusicapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.databinding.ItemMusicBinding


class SongAdapter(
    private val listener: (position: Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {


    private lateinit var context: Context
    private var songList = ArrayList<AudioFile>()

    inner class ViewHolder(var binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.binding.apply {
            musicNameTxtView.text = song.getTitle()
            if (song.getThumbnail() != null) {
                Glide.with(context)
                    .load(song.getThumbnail())
                    .into(thumbnail)
            }
            root.setOnClickListener {
                println("SongAdapter -> onBindViewHolder : ${song.getContentUri()}")
                listener.invoke(song.getPositionInSongList())
            }
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    fun setContext(context: Context) {
        this.context = context
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(songList: ArrayList<AudioFile>?) {
        if (songList != null) {
            this.songList = songList
            notifyDataSetChanged()
        }
    }
}

