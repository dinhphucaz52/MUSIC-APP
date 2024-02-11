package com.example.mymusicapp.repository.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.data.model.SongClass
import com.example.mymusicapp.R

class SongAdapter(
    private var songList: List<SongClass>,
    private val listener: (position: Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var preViewHolder: ViewHolder? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicNameTxtView: TextView = itemView.findViewById(R.id.musicNameTxtView)
        val durationTxtView: TextView = itemView.findViewById(R.id.durationTxtView)
        val itemBackground: LinearLayout = itemView.findViewById(R.id.itemBackground)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.musicNameTxtView.text = song.title
        holder.itemView.setOnClickListener {
            listener.invoke(position)
            preViewHolder?.itemView?.setBackgroundResource(R.drawable.item_background)
            holder.itemBackground.setBackgroundResource(R.drawable.item_background_clicked)
            preViewHolder = holder
        }
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

