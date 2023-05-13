package com.example.compose_music_player.ui.songsettingview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.compose_music_player.model.repository.SongRepository

class SongSettingFactory(private val songRepository: SongRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongSettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongSettingViewModel(songRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}