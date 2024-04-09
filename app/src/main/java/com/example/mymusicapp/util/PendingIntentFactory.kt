package com.example.mymusicapp.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.service.MusicService

object PendingIntentFactory {
    fun createPendingIntent(context: Context, requestCode: Int): PendingIntent {
        return PendingIntent.getService(
            context, requestCode, Intent(
                context, MusicService::class.java
            ).putExtra(AppCommon.REQUEST_CODE, requestCode), PendingIntent.FLAG_MUTABLE
        )
    }
}