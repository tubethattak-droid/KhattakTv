package com.khattaktv.app

object ChannelRepository {
    // Only add direct, authorized, playable stream URLs here.
    // Official live pages are not inserted as fake Media3 stream URLs.
    val channels = listOf(
        Channel("Khattak TV", "LIVE", emptyList()),

        Channel("PTV Home", "ENTERTAINMENT", emptyList()),
        Channel("PTV News", "NEWS", emptyList()),
        Channel("PTV Sports", "SPORTS", emptyList()),
        Channel("Geo News", "NEWS", emptyList()),
        Channel("ARY News", "NEWS", emptyList()),
        Channel("Samaa TV", "NEWS", emptyList()),
        Channel("Dunya News", "NEWS", emptyList()),
        Channel("Dawn News", "NEWS", emptyList()),
        Channel("Aaj News", "NEWS", emptyList()),
        Channel("92 News", "NEWS", emptyList()),
        Channel("Hum News", "NEWS", emptyList()),

        Channel("DD National", "ENTERTAINMENT", emptyList()),
        Channel("DD News", "NEWS", emptyList()),
        Channel("DD Sports", "SPORTS", emptyList()),
        Channel("DD India", "NEWS", emptyList()),
        Channel("DD Urdu", "ENTERTAINMENT", emptyList()),
        Channel("DD Kisan", "NEWS", emptyList()),
        Channel("DD Bharati", "ENTERTAINMENT", emptyList()),
        Channel("DD Punjabi", "ENTERTAINMENT", emptyList())
    )
}
