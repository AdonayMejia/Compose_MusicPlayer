package com.example.compose_music_player.ui.songsettingview.viewmodel

import android.app.Activity
import android.content.ContentUris
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.model.SongProvider.Companion.SongImgUri
import com.example.compose_music_player.model.SongProvider.Companion.SongName
import com.example.compose_music_player.model.SongProvider.Companion.SongProviderUri
import com.example.compose_music_player.model.SongProvider.Companion.SongUri
import com.example.compose_music_player.model.repository.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SongSettingViewModel(songRepository: SongRepository) : ViewModel() {
    private val _songs = MutableStateFlow(songRepository.getDefaultSongs())
    val songs: StateFlow<List<SongModel>> = _songs

    private val _deletedSongPosition = MutableStateFlow(0)
    val deletedSongPosition: StateFlow<Int>
    get() = _deletedSongPosition.asStateFlow()

    private fun removeSongFromHomeScreen(position: Int) {
        _songs.value = _songs.value.filterIndexed { index, _ -> index != position }
    }

    fun deleteSong(position: Int, activity: Activity) {
        val deleteUri = ContentUris.withAppendedId(SongProviderUri, position.toLong())
        activity.contentResolver.delete(deleteUri, null, null)

        _deletedSongPosition.value = position
        removeSongFromHomeScreen(position)
    }

//    fun resetDeletedSongPosition() {
//        _deletedSongPosition.postValue(null)
//    }

    fun addNewSongs(newSongs: List<SongModel>) {
        val nonDuplicateSongs = newSongs.filter { newSong ->
            !_songs.value.orEmpty().contains(newSong)
        }
        _songs.value = _songs.value.orEmpty() + nonDuplicateSongs
    }

    fun fetchSongsFromProvider(activity: Activity): MutableList<SongModel> {
        val cursor = activity.contentResolver.query(
            SongProviderUri, null, null, null, null
        )

        val songs = mutableListOf<SongModel>()

        cursor?.use {
            while (it.moveToNext()) {
                try {
                    val title = it.getString(it.getColumnIndexOrThrow(SongName))
                    val songUri = Uri.parse(it.getString(it.getColumnIndexOrThrow(SongUri)))
                    val albumArtUri =
                        Uri.parse(it.getString(it.getColumnIndexOrThrow(SongImgUri)))

                    val song = SongModel(title, songUri, albumArtUri)
                    if (!songs.contains(song)) {
                        songs.add(song)
                    }

                } catch (e: IllegalArgumentException) {
                    Log.e(
                        "SettingSongVM",
                        "Error reading song data from provider: ${e.message}"
                    )
                }
            }
        }
        return songs.distinct().toMutableList()
    }
}