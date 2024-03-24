package com.example.mymusicapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.R


class SongAdapter(
    private var songList: List<AudioFile>,
    private val listener: (position: Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicNameTxtView: TextView = itemView.findViewById(R.id.musicNameTxtView)
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.musicNameTxtView.text = song.title
        holder.thumbnail.setImageBitmap(song.thumbnail)
        holder.itemView.setOnClickListener {
            listener.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    @Deprecated("test")
    private fun getMp3Thumbnail(filePath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(filePath)
            val embeddedThumbnail = retriever.embeddedPicture

            return if (embeddedThumbnail != null) {
                BitmapFactory.decodeByteArray(embeddedThumbnail, 0, embeddedThumbnail.size)
            } else {
                BitmapFactory.decodeResource(
                    Resources.getSystem(),
                    R.drawable.item_ic_song
                )
            }
        } catch (e: Exception) {
            println("Error extracting thumbnail from MP3: ${e.message}")
            return null
        } finally {
            retriever.release()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(songList: ArrayList<AudioFile>?) {
        if (songList != null) {
            this.songList = songList
        }
        notifyDataSetChanged()
    }
}

