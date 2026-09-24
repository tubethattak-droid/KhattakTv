package com.khattaktv.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.exoplayer.ExoPlayer
import com.khattaktv.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        try {
            player = ExoPlayer.Builder(this).build()
            binding.playerView.player = player
        } catch (e: Exception) {
            player = null
            Toast.makeText(this, "Video player could not start", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStop() {
        binding.playerView.player = null
        player?.release()
        player = null
        super.onStop()
    }
}
