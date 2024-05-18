package com.example.mymusicapp.data.model

import android.graphics.Bitmap
import android.net.Uri

data class ImageFile(
    val uri: Uri,
    var bitmap: Bitmap? = null
)
