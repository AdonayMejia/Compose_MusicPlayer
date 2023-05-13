package com.example.compose_music_player.ui.homeview.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.compose_music_player.R
import com.example.compose_music_player.model.SongModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongList(
    songs: List<SongModel>,
    onSongClick: (SongModel) -> Unit,
    onRefresh: suspend () -> Unit,
    modifier: Modifier = Modifier
) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(songs.size) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        onRefresh()
        itemCount = songs.size
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(Modifier.pullRefresh(state)) {
        LazyColumn(Modifier.fillMaxSize()) {
            if (!refreshing) {
                items(songs) { song ->
                    SongListItem(song, onSongClick)
                    Divider()
                }
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}


@Composable
fun SongListItem(song : SongModel, onClick: (SongModel) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick(song) }
            .fillMaxSize()
            .padding(8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(model = song.image),
            contentDescription = stringResource(R.string.album_img),
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            song.name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge,

        )
    }
}


