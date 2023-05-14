package com.example.compose_music_player.model.bottombar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.compose_music_player.R

@Composable
fun BottomBarActions(
    onSettingsClick: () -> Unit,
    onStartSongList: () -> Unit,
    onPlayRandomSong: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(modifier = modifier) {
        IconButton(
            onClick = onSettingsClick,
        ) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = stringResource(R.string.settings),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onPlayRandomSong) {
            Icon(
                Icons.Filled.Shuffle,
                contentDescription = stringResource(R.string.random),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onStartSongList) {
            Icon(
                Icons.Filled.PlayCircle,
                contentDescription = stringResource(R.string.start_playlist),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
