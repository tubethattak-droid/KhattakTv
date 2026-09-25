package com.khattaktv.app

object ChannelRepository {
    // Direct public HLS sources collected from current public listings.
    // Sources can change or be geo-restricted; the player automatically tries
    // the next configured server when playback fails.
    val channels = listOf(
        Channel("Khattak TV", "LIVE", emptyList()),

        Channel("PTV Home", "ENTERTAINMENT", listOf(
            "https://www.ptv.com.pk/"
        )),
        Channel("PTV News", "NEWS", listOf(
            "https://www.moib.gov.pk/LiveStream"
        )),
        Channel("PTV Sports", "SPORTS", listOf(
            "https://www.ptv-sports.com.pk/live-streaming/"
        )),

        Channel("Geo News", "NEWS", listOf(
            "https://jk3lz82elw79-hls-live.5centscdn.com/GEONEWS/3500ba09d0538297440ca620c9dd46bf.sdp/playlist.m3u8"
        )),
        Channel("ARY News", "NEWS", listOf(
            "https://cdn07lhr.tamashaweb.com:8087/jazzauth/vsat-arynews-abr/live/vsat-arynews-H/chunks_dvr_timeshift-0-3600.m3u8"
        )),
        Channel("Dunya News", "NEWS", listOf(
            "https://intl.dunyanews.tv/livehd/ngrp:dunyalivehd_2_all/playlist.m3u8"
        )),
        Channel("Aaj News", "NEWS", listOf(
            "https://www.aaj.tv/live"
        )),

        Channel("DD National", "ENTERTAINMENT", listOf(
            "https://d3qs3d2rkhfqrt.cloudfront.net/out/v1/40492a64c1db4a1385ba1a397d357d3a/index.m3u8"
        )),
        Channel("DD News", "NEWS", listOf(
            "https://playhls.media.nic.in/live/ddnews/index.m3u8",
            "https://d3qs3d2rkhfqrt.cloudfront.net/out/v1/0811cd8c37ca4c409d5385a6cd2fa18b/index.m3u8"
        )),
        Channel("DD Sports", "SPORTS", listOf(
            "https://www.prasarbharati.gov.in/en/dd-sports-homepage/"
        )),
        Channel("DD India", "NEWS", listOf(
            "https://d3qs3d2rkhfqrt.cloudfront.net/out/v1/0811cd8c37ca4c409d5385a6cd2fa18b/index.m3u8"
        )),
        Channel("DD Urdu", "ENTERTAINMENT", emptyList()),
        Channel("DD Kisan", "NEWS", emptyList()),
        Channel("DD Bharati", "ENTERTAINMENT", listOf(
            "https://cdn-1.pishow.tv/live/10/master.m3u8"
        )),
        Channel("DD Punjabi", "ENTERTAINMENT", listOf(
            "https://d3qs3d2rkhfqrt.cloudfront.net/out/v1/da821c24a59d4e57960497aeaca8fb33/index.m3u8"
        ))
    )
}
