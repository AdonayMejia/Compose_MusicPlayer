package com.example.compose_music_player.ui.homeview.viewmodel

import com.example.compose_music_player.model.SongModel
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(
    val songStateFlow : StateFlow<List<SongModel>>
)