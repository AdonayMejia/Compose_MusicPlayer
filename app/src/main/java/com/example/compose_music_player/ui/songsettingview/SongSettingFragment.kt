package com.example.compose_music_player.ui.songsettingview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.compose_music_player.R
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.model.repository.SongRepository
import com.example.compose_music_player.ui.composeView
import com.example.compose_music_player.ui.songsettingview.component.SettingList
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingFactory
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingViewModel

class SongSettingFragment : Fragment() {

    private val viewModel: SongSettingViewModel by activityViewModels {
        SongSettingFactory(SongRepository)
    }
    private lateinit var songs: MutableList<SongModel>
    private val songState = mutableStateOf<List<SongModel>>(listOf())

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val onPressBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_songSettingFragment_to_homeViewFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onPressBack)

        viewModel.addNewSongsToProvider(requireActivity().contentResolver,
            requireActivity().resources, ::loadSongsFromProvider)
        loadSongsFromProvider()
        return composeView {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.addSelectedSongsToHomeList(
                                songState,
                                requireActivity().contentResolver
                            )
                        },
                        content = {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_song)
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                },
                floatingActionButtonPosition = FabPosition.End,
            ) { contentPadding ->
                activity?.let {
                    SettingList(
                        activity = it,
                        songsState = songState,
                        paddingValues = contentPadding,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun loadSongsFromProvider() {
        songs = viewModel.fetchSongsFromProvider(requireActivity())
        songState.value = viewModel.fetchSongsFromProvider(requireActivity())
    }

    companion object {
        const val SONG_NAME_FOUR: String = "Me Myself & I"
        const val SONG_NAME_FIVE: String = "Somebody Else"
        const val SONG_NAME_SIX: String = "Imagination"
        const val SONG_NAME_SEVEN: String = "Motley Crew"
        const val SONG_NAME_EIGHT: String = "Kemba Walker"
        const val SONG_NAME_NINE: String = "Iris"
        const val SONG_NAME_TEN: String = "Never Gonna Give You Up"
        const val SONG_NAME = "song_name"
        const val SONG_URI = "song_uri"
        const val ALBUM_ART_URI = "album_art_uri"
    }

}