package com.example.compose_music_player.ui.songplayerview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.compose_music_player.ui.homeview.viewmodel.HomeViewModel

class SongPlayerViewModelFactory(
    private val homeViewModel: HomeViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongPlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongPlayerViewModel(homeViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
