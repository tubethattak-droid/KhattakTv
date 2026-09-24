package com.khattaktv.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.khattaktv.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var player: ExoPlayer? = null
    private var currentChannel = 0
    private var currentServer = 0

    private val channels = ChannelRepository.channels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        startPlayer()
    }

    private fun startPlayer() {
        try {
            player?.release()
            player = ExoPlayer.Builder(this).build().also { exo ->
                binding.playerView.player = exo
                exo.addListener(object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        if (!moveToNextServer()) {
                            Toast.makeText(
                                this@MainActivity,
                                "No playable official source is available for this channel.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            player = null
            Toast.makeText(this, "Video player could not start", Toast.LENGTH_LONG).show()
        }
    }

    private fun playChannel(index: Int) {
        if (index !in channels.indices) return
        currentChannel = index
        currentServer = 0
        loadCurrentServer()
    }

    private fun loadCurrentServer() {
        val channel = channels.getOrNull(currentChannel) ?: return
        val url = channel.servers.getOrNull(currentServer) ?: return
        val exo = player ?: return
        exo.setMediaItem(MediaItem.fromUri(url))
        exo.prepare()
        exo.playWhenReady = true
    }

    private fun moveToNextServer(): Boolean {
        val channel = channels.getOrNull(currentChannel) ?: return false
        if (currentServer + 1 >= channel.servers.size) return false
        currentServer++
        loadCurrentServer()
        return true
    }

    override fun onStop() {
        binding.playerView.player = null
        player?.release()
        player = null
        super.onStop()
    }
}
