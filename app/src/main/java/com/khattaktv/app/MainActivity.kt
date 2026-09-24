package com.khattaktv.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.exoplayer.ExoPlayer
import com.khattaktv.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var player: ExoPlayer? = null
    private var currentServer = 0

    // Each channel can carry 2–3 stream URLs. When the active URL fails,
    // the player can advance to the next server without changing the channel.
    private val channels = listOf(
        Channel("Khattak TV", "PAKISTAN", listOf("SERVER_1", "SERVER_2", "SERVER_3")),
        Channel("Pakistan News", "NEWS", listOf("SERVER_1", "SERVER_2", "SERVER_3")),
        Channel("Pakistan Sports", "SPORTS", listOf("SERVER_1", "SERVER_2", "SERVER_3")),
        Channel("Indian Entertainment", "INDIA", listOf("SERVER_1", "SERVER_2", "SERVER_3"))
    )

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
            player = ExoPlayer.Builder(this).build().also {
                binding.playerView.player = it
                currentServer = 0
                // Stream URLs will be attached here as the channel catalogue is populated.
                // Server order is preserved for automatic fallback: 1 -> 2 -> 3.
            }
        } catch (e: Exception) {
            player = null
            Toast.makeText(this, "Video player could not start", Toast.LENGTH_LONG).show()
        }
    }

    private fun moveToNextServer(): Boolean {
        val channel = channels.firstOrNull() ?: return false
        if (currentServer + 1 >= channel.servers.size) return false
        currentServer++
        // The real stream URL for this server is selected here when the channel feed is configured.
        return true
    }

    override fun onStop() {
        binding.playerView.player = null
        player?.release()
        player = null
        super.onStop()
    }
}
