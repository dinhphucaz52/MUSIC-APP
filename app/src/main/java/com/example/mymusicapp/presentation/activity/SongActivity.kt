package com.example.mymusicapp.presentation.activity

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.databinding.ActivitySongBinding
import com.example.mymusicapp.presentation.viewmodel.MainViewModel


class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private val mainMVVM = MainViewModel.getInstance()
    private val controller = mainMVVM.getController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setEvents()

    }


    private fun loadDataFromMediaMetadata() {
        binding.apply {
            tvSongName.text = controller.mediaMetadata.title
            artistName.text = controller.mediaMetadata.artist ?: "Unknown Artist"
            seekBar.max = controller.duration.toInt()
            seekBar.progress = 0
            if (!isDestroyed)
                Glide.with(this@SongActivity)
                    .load(controller.mediaMetadata.artworkData)
                    .centerCrop()
                    .into(songImage)
            playButton.setImageResource(if (controller.isPlaying) R.drawable.pause else R.drawable.play)
            shuffleButton.setImageResource(if (controller.repeatMode == Player.REPEAT_MODE_ONE) R.drawable.repeat_one else R.drawable.shuffle)
        }
    }

    private fun setEvents() {
        binding.apply {
            nextButton.setOnClickListener {
                controller.seekToNext()
                controller.play()
            }
            previousButton.setOnClickListener {
                controller.seekToPrevious()
                controller.play()
            }
            playButton.setOnClickListener {
                if (controller.isPlaying)
                    controller.pause()
                else
                    controller.play()
            }
            shuffleButton.setOnClickListener {
                if (controller.repeatMode == Player.REPEAT_MODE_ONE) {
                    controller.repeatMode = Player.REPEAT_MODE_ALL
                    controller.shuffleModeEnabled = true
                } else {
                    controller.repeatMode = Player.REPEAT_MODE_ONE
                    controller.shuffleModeEnabled = false
                }
                shuffleButton.setImageResource(if (controller.repeatMode == Player.REPEAT_MODE_ONE) R.drawable.repeat_one else R.drawable.shuffle)
            }
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (seekBar != null) {
                        controller.seekTo(seekBar.progress.toLong())
                    }
                }
            }
            )
            backButton.setOnClickListener {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadDataFromMediaMetadata()
    }

    private fun init() {
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val controller = mainMVVM.getController()
        controller.addListener(object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                if (mediaMetadata.title != null) {
                    loadDataFromMediaMetadata()
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                binding.playButton.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
            }
        })
    }
}