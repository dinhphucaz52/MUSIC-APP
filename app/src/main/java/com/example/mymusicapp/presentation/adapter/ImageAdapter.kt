package com.example.mymusicapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.data.model.ImageFile
import com.example.mymusicapp.databinding.ItemImageBinding
import com.example.mymusicapp.callback.ItemListener

class ImageAdapter(
    private val context: Context,
    private val listener: ItemListener
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private var images = ArrayList<ImageFile>()

    inner class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClicked(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImageBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images[position].bitmap != null)
            holder.binding.imageViewGallery.setImageBitmap(images[position].bitmap)
    }

    fun addImage(imageFile: ImageFile) {
        images.add(imageFile)
        notifyItemInserted(images.size - 1)
    }
}