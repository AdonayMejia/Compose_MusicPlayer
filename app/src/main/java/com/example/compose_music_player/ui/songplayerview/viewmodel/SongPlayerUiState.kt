package com.example.compose_music_player.ui.songplayerview.viewmodel

import android.net.Uri
import com.example.compose_music_player.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SongPlayerUiState(
    val songName: StateFlow<String?> = MutableStateFlow(null),
    val image: StateFlow<Uri?> = MutableStateFlow(Uri.EMPTY),
    val index: StateFlow<Int> = MutableStateFlow(0),
    val playButton: StateFlow<Int> = MutableStateFlow(R.drawable.play),
    val sliderPosition: StateFlow<Float> = MutableStateFlow(0f)
)