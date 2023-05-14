package com.example.compose_music_player.ui.songsettingview.component

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.compose_music_player.R
import com.example.compose_music_player.model.SongModel
import com.example.compose_music_player.ui.songsettingview.viewmodel.SongSettingViewModel

@Composable
fun SettingList(
    viewModel: SongSettingViewModel,
    songsState: MutableState<List<SongModel>>,
    paddingValues: PaddingValues,
    activity: Activity,
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(
            horizontal = 0.dp,
            vertical = 8.dp
        )
    ) {
        items(songsState.value) { song ->
            SongListItem(
                song = song,
                onClick = { updatedSong ->
                    viewModel.checkBoxSongSelected(songsState, updatedSong)
                },
                onDeleteSong = {
                    viewModel.songDeletion(songsState, song, activity)
                },
            )
            Divider()
        }
    }
}


@Composable
fun SongListItem(
    song: SongModel,
    onClick: (SongModel) -> Unit,
    onDeleteSong: (SongModel) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(song.copy(selected = true)) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = song.image),
            contentDescription = stringResource(R.string.album_img),
            modifier = Modifier
                .size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(song.name, modifier = Modifier.weight(1f))

        Checkbox(
            checked = song.selected,
            onCheckedChange = { checked ->
                onClick(song.copy(selected = checked))
            }
        )
        IconButton(onClick = { onDeleteSong(song) }) {
            Icon(
                Icons.Default.Cancel,
                contentDescription = stringResource(R.string.delete_song),
                tint = MaterialTheme.colorScheme.error
            )
        }

    }
}