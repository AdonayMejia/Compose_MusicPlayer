package com.example.compose_music_player.ui.songplayerview.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_music_player.R
import com.example.compose_music_player.model.Player
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.ui.homeview.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongPlayerViewModel(
    val homeViewModel: HomeViewModel
) : ViewModel() {

    private var initSongName = false

    private val sliderPosition = MutableStateFlow(sliderPositionValue)

    private val songName = MutableStateFlow<String?>(null)

    private val currentSongIndex = MutableStateFlow(initialIndexValue)

    private val songAlbumImgUri = MutableStateFlow<Uri?>(null)

    private val playButtonMutableStateFlow = MutableStateFlow(R.drawable.pause)

    private val songs: StateFlow<List<SongModel>> = homeViewModel.uiState.songStateFlow

    val uiState = SongPlayerUiState(
        songName,
        songAlbumImgUri,
        currentSongIndex,
        playButtonMutableStateFlow,
        sliderPosition = sliderPosition.asStateFlow()
    )

    val currentSongPlaying: StateFlow<SongModel?> = currentSongIndex.map { index ->
        songs.value.getOrNull(index)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun updateSliderPosition(mediaPlayer: Player) {
        viewModelScope.launch {
            while (true) {
                mediaPlayer.mediaPlayer?.let { mediaPlayer ->
                    if (mediaPlayer.isPlaying) {
                        val currentPosition = mediaPlayer.currentPosition.toFloat()
                        val duration = mediaPlayer.duration.toFloat()
                        val progress = currentPosition / duration
                        sliderPosition.value = progress
                    }
                }
                delay(1000)
            }
        }
    }

    fun sliderPositionChanged(newSliderPosition: Float) {
        Player.mediaPlayer?.let { mediaPlayer ->
            val newPosition = (newSliderPosition * mediaPlayer.duration).toInt()
            mediaPlayer.seekTo(newPosition)
        }
    }

    fun initSongName(song: SongModel) {
        this.songName.value = song.name
        val index = songs.value.indexOfFirst { it == song }
        currentSongIndex.value = index
        songAlbumImgUri.value = song.image
        initSongName = true
    }

    fun onPlayPauseButtonClick() {
        Player.mediaPlayer?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playButtonMutableStateFlow.value = R.drawable.playbtn
            } else {
                mediaPlayer.start()
                playButtonMutableStateFlow.value = R.drawable.pause
            }
        }
    }

    fun onPreviousButtonClick(
        context: Context,
        mediaPlayer: Player
    ) {
        viewModelScope.launch {
            songs.collect { songs ->
                val currentSongIndexValue = currentSongIndex.value
                val newSongIndex = if (currentSongIndexValue > 0) {
                    currentSongIndexValue - 1
                } else {
                    songs.size - 1
                }
                currentSongIndex.value = newSongIndex
                val newSong = songs.getOrNull(newSongIndex)
                newSong?.let {
                    initSongName(it)
                    playSong(context, mediaPlayer)
                }
            }
        }
    }

    fun onNextButtonClick(
        context: Context,
        mediaPlayer: Player
    ) {
        viewModelScope.launch {
            songs.collect { songs ->
                val currentSongIndexValue = currentSongIndex.value
                val newSongIndex = if (currentSongIndexValue < songs.size - 1) {
                    currentSongIndexValue + 1
                } else {
                    initialIndexValue
                }
                currentSongIndex.value = newSongIndex
                val newSong = songs.getOrNull(newSongIndex)
                newSong?.let {
                    initSongName(it)
                    playSong(context, mediaPlayer)
                }
            }
        }
    }

    private fun playSong(context: Context, mediaPlayer: Player) {
        mediaPlayer.mediaPlayer?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()

            val songs = homeViewModel.uiState.songStateFlow.value

            if (currentSongIndex.value !in songs.indices) {
                currentSongIndex.value = initialIndexValue
            }

            val song = songs[currentSongIndex.value]
            mediaPlayer.setDataSource(context, song.resource)
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                playButtonMutableStateFlow.value = R.drawable.pause
                initSongName(song)
            }
        }
    }

    companion object {
        const val sliderPositionValue = 0f
        const val initialIndexValue = 0
    }
}
