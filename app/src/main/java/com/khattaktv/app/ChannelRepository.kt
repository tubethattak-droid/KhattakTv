package com.khattaktv.app

data class Channel(
    val name: String,
    val streamUrl: String
)

object ChannelRepository {
    // Replace these placeholders with authorized public stream URLs when available.
    val channels = listOf(
        Channel("Khattak TV", "")
    )
}
