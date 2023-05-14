package com.example.compose_music_player.ui.homeview.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.model.repository.SongRepository
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val songRepository: SongRepository,
    context: Context,
    private val songSettingViewModel: SongSettingViewModel
) : ViewModel() {
    init {
        SongRepository.init(context)
    }

    private val songsMutableState = MutableStateFlow<List<SongModel>>(emptyList())
    val uiState = HomeUiState(songsMutableState)

    init {
        viewModelScope.launch {
            fetchSongs(context as Activity)
            collectAddedSongs()
        }
    }

    fun onSongClick(song: SongModel): Int {
        return uiState.songStateFlow.value.indexOf(song)
    }

    private suspend fun collectAddedSongs() {
        songSettingViewModel.songsAdded.collect { song ->
            songsMutableState.value = songsMutableState.value + song
        }
    }

    fun refreshSongs() {
        viewModelScope.launch {
            val defaultSongs = songRepository.getDefaultSongs()
            songsMutableState.value = defaultSongs
        }
    }

    private fun fetchSongs(activity: Activity) {
        val defaultSongs = songRepository.getDefaultSongs()

        val providerSongs = songSettingViewModel.fetchSongsFromProvider(activity)

        val combinedSongs = mutableListOf<SongModel>().apply {
            addAll(defaultSongs)
            addAll(providerSongs.filter { song -> !defaultSongs.contains(song) })
        }
        songsMutableState.value = combinedSongs
    }

    fun playFirstSong(): SongModel? {
        val songs = uiState.songStateFlow.value
        return if (songs.isNotEmpty()) {
            songs[0]
        } else {
            null
        }
    }

    fun randomStart(): SongModel? {
        val songs = uiState.songStateFlow.value
        return if (songs.isNotEmpty()) {
            val random = (songs.indices).random()
            songs[random]
        } else {
            null
        }
    }
}
