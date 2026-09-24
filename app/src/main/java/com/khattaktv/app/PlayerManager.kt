package com.khattaktv.app

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object PlayerManager {
    fun create(context: Context): ExoPlayer = ExoPlayer.Builder(context).build()

    fun play(player: ExoPlayer, url: String) {
        if (url.isBlank()) return
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.playWhenReady = true
    }
}
