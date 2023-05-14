package com.example.compose_music_player.ui.songsettingview.viewmodel

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_music_player.R
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.model.SongProvider.Companion.SongImgUri
import com.example.compose_music_player.model.SongProvider.Companion.SongName
import com.example.compose_music_player.model.SongProvider.Companion.SongProviderUri
import com.example.compose_music_player.model.SongProvider.Companion.SongUri
import com.example.compose_music_player.model.repository.SongRepository
import com.example.compose_music_player.ui.songsettingview.SongSettingFragment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongSettingViewModel(songRepository: SongRepository) : ViewModel() {
    private val _songs = MutableStateFlow(songRepository.getDefaultSongs())
    val songs: StateFlow<List<SongModel>> = _songs

    private val _songsAdded = MutableSharedFlow<SongModel>()
    val songsAdded: SharedFlow<SongModel> = _songsAdded

    fun fetchSongsFromProvider(activity: Activity): MutableList<SongModel> {
        val cursor = activity.contentResolver.query(
            SongProviderUri, null, null, null, null
        )

        val songs = mutableListOf<SongModel>()

        cursor?.use {
            while (it.moveToNext()) {
                try {
                    val name = it.getString(it.getColumnIndexOrThrow(SongName))
                    val songUri = Uri.parse(it.getString(it.getColumnIndexOrThrow(SongUri)))
                    val albumImgUri =
                        Uri.parse(it.getString(it.getColumnIndexOrThrow(SongImgUri)))

                    val song = SongModel(name, songUri, albumImgUri)
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

    private fun addNewSongs(newSongs: List<SongModel>) {
        val nonDuplicateSongs = newSongs.filter { newSong ->
            !_songs.value.any { existingSong ->
                existingSong.name == newSong.name
            }
        }
        _songs.value = _songs.value + nonDuplicateSongs

        viewModelScope.launch {
            nonDuplicateSongs.forEach { song ->
                _songsAdded.emit(song)
            }
        }
    }

    fun addSelectedSongsToHomeList(
        songsState: MutableState<List<SongModel>>,
        contentResolver: ContentResolver
    ) {
        val songsSelected = songsState.value.filter { it.selected }
        addNewSongs(songsSelected)

        for (song in songsSelected) {
            val contentValues = ContentValues().apply {
                put(SongName, song.name)
                put(SongUri, song.resource.toString())
                put(SongImgUri, song.image.toString())
            }
            contentResolver.insert(SongProviderUri, contentValues)
        }

        songsState.value = songsState.value.map { song ->
            song.copy(selected = false)
        }
    }

    fun songDeletion(
        songsState: MutableState<List<SongModel>>,
        song: SongModel,
        activity: Activity
    ) {
        val songIndex = songsState.value.indexOf(song)
        if (songIndex >= 0) {
            val songUriWithId = ContentUris.withAppendedId(SongProviderUri, songIndex.toLong())
            activity.contentResolver.delete(songUriWithId, null, null)
            songsState.value = songsState.value.toMutableList().apply {
                removeAt(songIndex)
            }
        }
    }

    fun addNewSongsToProvider(
        contentResolver: ContentResolver,
        resources: android.content.res.Resources,
        loadSongsFromProvider: () -> Unit
    ) {

        val newSongs = listOf(
            SongModel.create(SongSettingFragment.SONG_NAME_FOUR, R.raw.song4, R.drawable.myself),
            SongModel.create(SongSettingFragment.SONG_NAME_FIVE, R.raw.song5, R.drawable.somebody),
            SongModel.create(
                SongSettingFragment.SONG_NAME_SIX,
                R.raw.song6,
                R.drawable.imagination
            ),
            SongModel.create(SongSettingFragment.SONG_NAME_SEVEN, R.raw.song7, R.drawable.motley),
            SongModel.create(SongSettingFragment.SONG_NAME_EIGHT, R.raw.song8, R.drawable.walker),
            SongModel.create(SongSettingFragment.SONG_NAME_NINE, R.raw.song9, R.drawable.iris),
            SongModel.create(SongSettingFragment.SONG_NAME_TEN, R.raw.song10, R.drawable.up),
        )
        newSongs.forEach { song ->
            val contentValues = ContentValues().apply {
                put(SongSettingFragment.SONG_NAME, song.name)
                put(SongSettingFragment.SONG_URI, song.resource.toString())
                put(SongSettingFragment.ALBUM_ART_URI, song.image.toString())
            }
            contentResolver.insert(SongProviderUri, contentValues)
        }
        loadSongsFromProvider()
    }


    fun checkBoxSongSelected(songsState: MutableState<List<SongModel>>, updatedSong: SongModel) {
        var index = -1
        for (i in songsState.value.indices) {
            if (songsState.value[i].resource == updatedSong.resource) {
                index = i
                break
            }
        }
        if (index >= 0) {
            songsState.value = songsState.value.toMutableList().apply {
                this[index] = updatedSong.copy(selected = updatedSong.selected)
            }
        }
    }

}
