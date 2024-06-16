package com.example.mymusicapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.callback.ItemListener
import com.example.mymusicapp.data.model.PlayList
import com.example.mymusicapp.databinding.ItemPlayListBinding

class PlayListAdapter(
    context: Context,
    private val listener: ItemListener
) : RecyclerView.Adapter<PlayListAdapter.ViewHolder>() {
    private var playListList = ArrayList<PlayList>()

    inner class ViewHolder(val binding: ItemPlayListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                setOnClickListener {
                    listener.onItemClicked(position = bindingAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPlayListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = playListList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            textPlaylistTitle.text = playListList[position].name
            textViewSongs.text = "${playListList[position].songs.size} songs"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(playList: ArrayList<PlayList>) {
        this.playListList = playList
        notifyDataSetChanged()
    }
}
