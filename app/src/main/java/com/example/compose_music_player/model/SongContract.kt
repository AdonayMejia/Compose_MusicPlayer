package com.example.compose_music_player.model

import android.net.Uri

object SongContract {
    private const val AUTHORITY = "com.example.compose_music_player.provider"
    private const val BasePath = "songs"
    val ContentUri: Uri = Uri.parse("content://$AUTHORITY/$BasePath")

    object Columns {
        const val ID = "_id"
        const val SONG_NAME = "song_name"
        const val SONG_URI = "song_uri"
        const val ALBUM_IMG_URI = "album_img_uri"
    }
}