package com.example.compose_music_player.ui.songplayerview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.compose_music_player.model.Player
import com.example.compose_music_player.model.repository.SongRepository
import com.example.compose_music_player.ui.composeView
import com.example.compose_music_player.ui.homeview.viewmodel.HomeViewModel
import com.example.compose_music_player.ui.homeview.viewmodel.HomeViewModelFactory
import com.example.compose_music_player.ui.songplayerview.component.MediaPlayer
import com.example.compose_music_player.ui.songplayerview.viewmodel.SongPlayerViewModel
import com.example.compose_music_player.ui.songplayerview.viewmodel.SongPlayerViewModelFactory
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingFactory
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingViewModel

@Suppress("DEPRECATION")
class SongPlayerFragment : Fragment() {

    private val viewModel: SongPlayerViewModel by activityViewModels {
        SongPlayerViewModelFactory(
            homeViewModel
        )
    }
    private val songSettingViewModel: SongSettingViewModel by activityViewModels {
        SongSettingFactory(SongRepository)
    }
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(SongRepository, requireContext(), songSettingViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return composeView{
            MediaPlayer(viewModel = viewModel, player = Player)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSongInfo()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setInitialSongTitle()
    }

    private fun setInitialSongTitle() {
        val args = arguments
        val newSongTitle = args?.getString(SongKey).orEmpty()
        viewModel.homeViewModel.uiState.songStateFlow.value.find { it.name == newSongTitle }
            ?.let { song ->
                viewModel.initSongName(song)
            }
    }

    private fun startSongInfo() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currentSongPlaying.collect { song ->
                if (song != null) {
                    viewModel.initSongName(song)
                }
            }
        }
    }

    companion object {
        const val SongKey = "songTitle"
    }
}