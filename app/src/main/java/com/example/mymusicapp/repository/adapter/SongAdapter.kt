package com.example.mymusicapp.repository.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.data.model.SongClass
import com.example.mymusicapp.R

class SongAdapter(
    private var songList: List<SongClass>,
    private val listener: (position: Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val musicNameTxtView: TextView = itemView.findViewById(R.id.musicNameTxtView)
        val durationTxtView: TextView = itemView.findViewById(R.id.durationTxtView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.invoke(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.musicNameTxtView.text = song.title
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(songList: ArrayList<SongClass>?) {
        if (songList != null) {
            this.songList = songList
        };
        notifyDataSetChanged()
    }

}

