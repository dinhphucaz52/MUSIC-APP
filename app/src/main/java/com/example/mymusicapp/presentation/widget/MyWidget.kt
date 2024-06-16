package com.example.mymusicapp.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.R
import com.example.mymusicapp.data.service.MusicService


@UnstableApi
class MyWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        println("MyWidget.onUpdate")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        println("MyWidget.onEnabled")
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        println("MyWidget.onDisabled")
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.my_widget)
        val intent = Intent(context, MusicService::class.java)
        intent.action = "PLAY"
        views.setOnClickPendingIntent(
            R.id.widget_button_next,
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
