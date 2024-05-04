package com.example.mymusicapp.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.databinding.ActivitySongBinding
import com.example.mymusicapp.presentation.viewmodel.MainViewModel


@Suppress("DEPRECATION")
class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private val mainMVVM = MainViewModel.getInstance()
    private val controller = mainMVVM.getController()
    private val handler by lazy {
        Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setEvents()

        this@SongActivity.runOnUiThread(object : Runnable {
            override fun run() {
                binding.seekbar.progress = controller.currentPosition.toInt()
                handler.postDelayed(this, 1000)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        loadDataFromMediaMetadata()
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
                if (controller.shuffleModeEnabled) {
                    controller.shuffleModeEnabled = false
                    controller.repeatMode = Player.REPEAT_MODE_ALL
                } else {
                    if (controller.repeatMode == Player.REPEAT_MODE_ALL) {
                        controller.repeatMode = Player.REPEAT_MODE_ONE
                    } else {
                        controller.repeatMode = Player.REPEAT_MODE_ALL
                        controller.shuffleModeEnabled = true
                    }
                }
                updateShuffleButton()
            }

            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
                        controller.play()
                    }
                }
            }
            )

            backButton.setOnClickListener {
                finish()
            }

        }
    }

    private fun init() {
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        controller.addListener(object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                if (mediaMetadata.title != null) {
                    loadDataFromMediaMetadata()
                }
            }
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                updatePlayButton()
            }
        })
    }

    @SuppressLint("Recycle")
    private fun loadDataFromMediaMetadata() {
        binding.apply {
            tvSongName.apply {
                text = controller.mediaMetadata.title
                println("SongActivity: ${controller.mediaMetadata.title}")
//                val animator = AnimatorFactory.runningAnimation(tvSongName)
//                animator.start()
            }
            artistName.text = controller.mediaMetadata.artist ?: "Unknown Artist"
            seekbar.max = controller.duration.toInt()
            seekbar.progress = controller.currentPosition.toInt()
            if (!isDestroyed) Glide.with(this@SongActivity)
                .load(controller.mediaMetadata.artworkData).centerCrop().into(songImage)
            updatePlayButton()
            updateShuffleButton()
        }
    }

    private fun updatePlayButton() {
        binding.playButton.setImageResource(
            if (controller.isPlaying) R.drawable.pause
            else R.drawable.play
        )
    }

    private fun updateShuffleButton() {
        binding.shuffleButton.setImageResource(
            if (controller.shuffleModeEnabled) R.drawable.shuffle
            else when (controller.repeatMode) {
                Player.REPEAT_MODE_ALL -> R.drawable.repeat
                else -> R.drawable.repeat_one
            }
        )
    }

}