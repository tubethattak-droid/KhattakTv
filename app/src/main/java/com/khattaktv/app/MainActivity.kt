package com.khattaktv.app

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.khattaktv.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var player: ExoPlayer? = null
    private var playerView: PlayerView? = null
    private var currentChannel = 0
    private var currentServer = 0
    private val channels = ChannelRepository.channels
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            buildChannelCards()
            showStartupLoader()
        } catch (e: Exception) {
            val message = e.message ?: e.javaClass.simpleName
            setContentView(TextView(this).apply {
                text = "Khattak TV\nStartup error: $message"
                setTextColor(Color.WHITE)
                textSize = 18f
                gravity = Gravity.CENTER
                setBackgroundColor(Color.rgb(8, 9, 12))
                setPadding(40, 40, 40, 40)
            })
        }
    }

    private fun showStartupLoader() {
        binding.splashOverlay.visibility = View.VISIBLE
        binding.splashLogo.alpha = 0f
        binding.splashLogo.scaleX = 0.82f
        binding.splashLogo.scaleY = 0.82f
        binding.splashLogo.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(650)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        mainHandler.postDelayed({
            if (isFinishing || isDestroyed) return@postDelayed
            binding.splashOverlay.animate()
                .alpha(0f)
                .setDuration(350)
                .withEndAction {
                    binding.splashOverlay.visibility = View.GONE
                    binding.splashOverlay.alpha = 1f
                }
                .start()
        }, 1100L)
    }

    private fun ensurePlayer(): Boolean {
        if (player != null) return true
        return try {
            val view = PlayerView(this).apply {
                useController = true
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            binding.playerContainer.removeAllViews()
            binding.playerContainer.addView(view)
            playerView = view

            player = ExoPlayer.Builder(this).build().also { exo ->
                view.player = exo
                exo.addListener(object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        if (!moveToNextServer()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Stream unavailable. Trying another source was not successful.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
            }
            true
        } catch (e: Exception) {
            player?.release()
            player = null
            playerView = null
            binding.playerContainer.removeAllViews()
            Toast.makeText(this, "Video player could not start", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun buildChannelCards() {
        binding.channelContainer.removeAllViews()
        channels.forEachIndexed { index, channel ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(18, 14, 18, 14)
                background = GradientDrawable().apply {
                    cornerRadius = 24f
                    setColor(Color.rgb(24, 28, 38))
                    setStroke(2, Color.rgb(52, 58, 72))
                }
                isFocusable = true
                isClickable = true
                setOnFocusChangeListener { view, focused ->
                    val bg = view.background as GradientDrawable
                    bg.setColor(if (focused) Color.rgb(229, 57, 53) else Color.rgb(24, 28, 38))
                    bg.setStroke(2, if (focused) Color.rgb(255, 193, 7) else Color.rgb(52, 58, 72))
                    view.animate().scaleX(if (focused) 1.05f else 1f).scaleY(if (focused) 1.05f else 1f).setDuration(120).start()
                }
                setOnClickListener { playChannel(index) }
            }
            card.layoutParams = LinearLayout.LayoutParams(190, 138).apply { marginEnd = 14 }

            val badge = TextView(this).apply {
                text = when (channel.category) {
                    "NEWS" -> "N"
                    "SPORTS" -> "S"
                    "ENTERTAINMENT" -> "E"
                    else -> "K\nTV"
                }
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                textSize = 16f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                background = GradientDrawable().apply {
                    cornerRadius = 18f
                    setColor(if (channel.name == "Khattak TV") Color.rgb(229, 57, 53) else Color.rgb(44, 50, 64))
                }
                layoutParams = LinearLayout.LayoutParams(62, 62)
            }
            card.addView(badge)

            val title = TextView(this).apply {
                text = channel.name
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                textSize = 15f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                maxLines = 1
                ellipsize = android.text.TextUtils.TruncateAt.END
                layoutParams = LinearLayout.LayoutParams(-1, -2).apply { topMargin = 10 }
            }
            card.addView(title)

            val subtitle = TextView(this).apply {
                text = "${channel.category} • ${if (channel.servers.isEmpty()) "SOURCE PENDING" else "LIVE"}"
                gravity = Gravity.CENTER
                setTextColor(Color.rgb(143, 149, 160))
                textSize = 9f
                layoutParams = LinearLayout.LayoutParams(-1, -2)
            }
            card.addView(subtitle)
            binding.channelContainer.addView(card)
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
        val url = channel.servers.getOrNull(currentServer)
        if (url.isNullOrBlank()) {
            Toast.makeText(this, "${channel.name}: no playable public source is configured yet.", Toast.LENGTH_SHORT).show()
            return
        }
        if (!ensurePlayer()) return
        val exo = player ?: return
        try {
            exo.stop()
            exo.setMediaItem(MediaItem.fromUri(url))
            exo.prepare()
            exo.playWhenReady = true
        } catch (e: Exception) {
            if (!moveToNextServer()) {
                Toast.makeText(this, "Unable to load this channel.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToNextServer(): Boolean {
        val channel = channels.getOrNull(currentChannel) ?: return false
        while (currentServer + 1 < channel.servers.size) {
            currentServer++
            val url = channel.servers.getOrNull(currentServer)
            if (!url.isNullOrBlank()) {
                loadCurrentServer()
                return true
            }
        }
        return false
    }

    override fun onStop() {
        playerView?.player = null
        player?.release()
        player = null
        playerView = null
        super.onStop()
    }

    override fun onDestroy() {
        mainHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
