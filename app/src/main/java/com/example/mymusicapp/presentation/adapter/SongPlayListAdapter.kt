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
    private var songList: ArrayList<SongFile>,
    private val callback: ItemListener
) : RecyclerView.Adapter<SongPlayListAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemSongPlayListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.checkBox.setOnClickListener {
                if (binding.checkBox.isChecked)
                    callback.onItemClicked(bindingAdapterPosition) // Add item to play list
                else
                    callback.onItemClicked(-bindingAdapterPosition) // Remove item from play list
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
            musicNameTxtView.text = song.getTitle()
            if (song.getThumbnail() != null) {
                Glide.with(context)
                    .load(song.getThumbnail())
                    .into(thumbnail)
            }
        }
    }
}