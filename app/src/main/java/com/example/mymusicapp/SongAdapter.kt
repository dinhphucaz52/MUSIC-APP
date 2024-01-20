package com.example.mymusicapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private val songList: List<SongClass>,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        val song = songList[position]
        holder.musicNameTxtView.text = song.title
    }

    override fun getItemCount(): Int {
        return songList.size
    }


}

