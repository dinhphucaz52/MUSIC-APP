package com.example.mymusicapp.repository.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.R
import com.example.mymusicapp.repository.myobject.Thumbnail


class SongAdapter(
    private var songList: List<AudioFile>,
    private val listener: (position: Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var preViewHolder: ViewHolder? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicNameTxtView: TextView = itemView.findViewById(R.id.musicNameTxtView)
        //        val durationTxtView: TextView = itemView.findViewById(R.id.durationTxtView)
        val itemBackground: LinearLayout = itemView.findViewById(R.id.itemBackground)
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
//        holder.thumbnail.setImageBitmap(getMp3Thumbnail(songList[position].path))
        holder.thumbnail.setImageBitmap(Thumbnail.getMp3Thumbnail(songList[position].path))
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

