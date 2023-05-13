package com.example.compose_music_player.ui.homeview.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.compose_music_player.model.repository.SongRepository
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingViewModel

class HomeViewModelFactory(
    private val songRepository: SongRepository,
    private val context: Context,
    private val songSettingViewModel: SongSettingViewModel) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(songRepository,context,songSettingViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
