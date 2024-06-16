package com.example.mymusicapp.presentation.activity

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mymusicapp.callback.ItemListener
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.model.ImageFile
import com.example.mymusicapp.data.repository.GalleryRepository
import com.example.mymusicapp.databinding.ActivityGalleryBinding
import com.example.mymusicapp.helper.BitmapHelper
import com.example.mymusicapp.presentation.adapter.ImageAdapter
import com.example.mymusicapp.presentation.viewmodel.GalleryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var galleryRepository: GalleryRepository
    private val imageAdapter by lazy {
        ImageAdapter(this@GalleryActivity, object : ItemListener {
            override fun onItemClicked(position: Int) {
                binding.apply {
                    containerImageView.visibility = View.VISIBLE
                    if (!isDestroyed) {
                        Glide.with(this@GalleryActivity)
                            .load(imageList[position].uri)
                            .into(imageViewPreview)
                    }
                }
            }
        })
    }
    private val imageList = ArrayList<ImageFile>()


    private val galleryMVVM by lazy {
        GalleryViewModel.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
        requestPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("GalleryActivity.onRequestPermissionsResult: $permissions")
        if (requestCode == AppCommon.REQUEST_CODE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getImages()
        }
    }

    private fun getImages() {
        CoroutineScope(Dispatchers.Default).launch {
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val contentResolver = contentResolver
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
            )
            val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val imageUri = Uri.withAppendedPath(uri, id.toString())
                    val imageFile = ImageFile(
                        imageUri,
                        BitmapHelper.getBitmap(this@GalleryActivity, imageUri)
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        imageAdapter.addImage(imageFile)
                        imageList.add(imageFile)
                    }
                }
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                getImages()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    AppCommon.REQUEST_CODE_PERMISSION
                )
            }
        }
    }

    private fun init() {
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        galleryRepository = GalleryRepository(this@GalleryActivity)
        galleryMVVM.setRepository(galleryRepository)
        prepareRecyclerView()
    }

    private fun prepareRecyclerView() {
        binding.recyclerViewGallery.apply {
            adapter = imageAdapter
            layoutManager =
                GridLayoutManager(this@GalleryActivity, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun setEvents() {
        binding.apply {
            fab.setOnClickListener {
                galleryMVVM.uploadImages(imageList)
            }
        }
    }
}