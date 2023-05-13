package com.example.compose_music_player.model.repository

import android.annotation.SuppressLint
import android.content.Context
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.model.SongProvider

@SuppressLint("StaticFieldLeak")
object SongRepository {
    private lateinit var context: Context
    var song: List<SongModel> = listOf()

    fun init(context: Context) {
        this.context = context
        val contentResolver = context.contentResolver
        contentResolver.query(
            SongProvider.SongProviderUri, null, null, null, null
        )?.let {
            song = SongProvider.getSongsFromCursor(it)
            it.close()
        }
    }
    fun getDefaultSongs(): List<SongModel> {
        return song.take(3)
    }
}