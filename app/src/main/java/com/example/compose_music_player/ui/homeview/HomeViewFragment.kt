package com.example.compose_music_player.ui.homeview

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.compose_music_player.R
import com.example.compose_music_player.model.Player
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.model.bottombar.BottomBarActions
import com.example.compose_music_player.model.repository.SongRepository
import com.example.compose_music_player.ui.composeView
import com.example.compose_music_player.ui.homeview.component.SongList
import com.example.compose_music_player.ui.homeview.viewmodel.HomeViewModel
import com.example.compose_music_player.ui.homeview.viewmodel.HomeViewModelFactory
import com.example.compose_music_player.ui.songplayerview.SongPlayerFragment
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingFactory
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingViewModel

class HomeViewFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(SongRepository,requireContext(),sharedViewModel)
    }
    private var currentSongIndex: Int = 0
    private val sharedViewModel: SongSettingViewModel by activityViewModels {
        SongSettingFactory(SongRepository)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return composeView {
            val songs by viewModel.uiState.songStateFlow.collectAsState()
            Scaffold(
                bottomBar = {
                    BottomBarActions(
                        onSettingsClick = { navigateToSetting() },
                        onStartSongList = { startPlaylist() },
                        onPlayRandomSong = { toggleRandomStart() }
                    )
                }
            ) {
                SongList(
                    songs = songs,
                    onSongClick = { song -> onSongClick(song)},
                    onRefresh = { viewModel.refreshSongs() },
                    modifier = Modifier.padding(it)
                )
            }
        }
    }

    private fun startPlaylist() {
        viewModel.playFirstSong()?.let {song ->
            onSongClick(song)
        }
    }

    private fun toggleRandomStart() {
        viewModel.randomStart()?.let {song ->
            onSongClick(song)
        }
    }

    private fun onSongClick(song: SongModel) {
        val position = viewModel.onSongClick(song)
        playSelectedSong(position)
        navigateToDetailActivity(position)
    }

    private fun playSelectedSong(position: Int) {
        Player.mediaPlayer?.release()
        currentSongIndex = position
        Player.mediaPlayer =
            MediaPlayer.create(context, viewModel.uiState.songStateFlow.value[position].resource)
        Player.mediaPlayer?.start()
    }

     private fun navigateToDetailActivity(position: Int) {
         val songs = viewModel.uiState.songStateFlow.value
         val bundle = Bundle().apply {
             putString(SongPlayerFragment.SongKey, songs[position].name)
         }
        findNavController().navigate(R.id.action_homeViewFragment_to_songPlayerFragment,bundle)
    }

    private fun navigateToSetting(){
        findNavController().navigate(R.id.action_homeViewFragment_to_songSettingFragment)
    }
}
