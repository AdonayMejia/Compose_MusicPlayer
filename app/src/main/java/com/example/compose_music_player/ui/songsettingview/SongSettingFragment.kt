package com.example.compose_music_player.ui.songsettingview

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.compose_music_player.R
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.model.SongProvider.Companion.SongProviderUri
import com.example.compose_music_player.model.repository.SongRepository
import com.example.compose_music_player.ui.songsettingview.component.SettingView
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingFactory
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingViewModel
import kotlinx.coroutines.launch

class SongSettingFragment : Fragment() {

    private val viewModel: SongSettingViewModel by activityViewModels {
        SongSettingFactory(SongRepository)
    }
    private lateinit var songs: MutableList<SongModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            addNewSongsToProvider()
            //loadSongsFromProvider()
            deleteSongObserver()
            setContent {
                SettingView()
            }
        }
    }

    private fun deleteSongObserver() {
        viewModel.viewModelScope.launch {
            viewModel.deletedSongPosition.value
        }
    }


    private fun onDeleteButtonClick(position: Int) {
        viewModel.deleteSong(position, requireActivity())
    }

//    private fun onSongClickListener(position: Int) {
//        songs[position].selected = !songs[position].selected
//        recyclerView.adapter?.notifyItemChanged(position)
//    }

//    private fun addSongs() {
//        val selectedSongs = songs.filter { it.selected }
//        val nonDuplicateSongs = selectedSongs.filter { newSong ->
//            !viewModel.songs.value.orEmpty().any { existingSong ->
//                existingSong.name == newSong.name
//            }
//        }
//        viewModel.addNewSongs(nonDuplicateSongs)
//
//        for (song in nonDuplicateSongs) {
//            val contentValues = ContentValues().apply {
//                put(SONG_NAME, song.name)
//                put(SONG_URI, song.resource.toString())
//                put(ALBUM_ART_URI, song.image.toString())
//            }
//            requireActivity().contentResolver.insert(SONG_PROVIDER_URI, contentValues)
//        }
//        songs.forEachIndexed { index, song ->
//            if (song.selected) {
//                song.selected = false
//                recyclerView.adapter?.notifyItemChanged(index)
//            }
//        }
//    }

    private fun addNewSongsToProvider() {

        val newSongs = listOf(
            SongModel.create(SONG_NAME_FOUR, R.raw.song4, R.drawable.myself),
//            SongModel.create(SONG_NAME_FIVE, R.raw.song5, R.drawable.somebody),
//            SongModel.create(SONG_NAME_SIX, R.raw.song6, R.drawable.imagination),
//            SongModel.create(SONG_NAME_SEVEN, R.raw.song7, R.drawable.motley),
//            SongModel.create(SONG_NAME_EIGHT, R.raw.song8, R.drawable.walker),
//            SongModel.create(SONG_NAME_NINE, R.raw.song9, R.drawable.iris),
//            SongModel.create(SONG_NAME_TEN, R.raw.song10, R.drawable.up),
        )
        newSongs.forEach { song ->
            val contentValues = ContentValues().apply {
                put(SONG_NAME, song.name)
                put(SONG_URI, song.resource.toString())
                put(ALBUM_ART_URI, song.image.toString())
            }
            requireActivity().contentResolver.insert(SongProviderUri, contentValues)
        }
        //loadSongsFromProvider()
    }

    private fun loadSongsFromProvider() {
        songs = viewModel.fetchSongsFromProvider(requireActivity())
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