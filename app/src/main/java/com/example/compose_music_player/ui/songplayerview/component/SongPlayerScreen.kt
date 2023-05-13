package com.example.compose_music_player.ui.songplayerview.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.compose_music_player.R
import com.example.compose_music_player.model.Player
import com.example.compose_music_player.ui.songplayerview.viewmodel.SongPlayerUiState
import com.example.compose_music_player.ui.songplayerview.viewmodel.SongPlayerViewModel


@Composable
fun MediaPlayer(
    viewModel : SongPlayerViewModel,
    player: Player
){
    LaunchedEffect(Unit) {
        viewModel.updateSliderPosition(player)
    }

    val context = LocalContext.current

    Player(
        uiState = viewModel.uiState,
        onPrevSong = {
            viewModel.onPreviousButtonClick(context,player)
        },
        onNextSong = {
            viewModel.onNextButtonClick(context, player)
        },
        onPlaySong = viewModel::onPlayPauseButtonClick,
        positionSliderChanged = {sliderPosition ->
            viewModel.sliderPositionChanged(sliderPosition)
        }
    )
}

@Composable
fun Player(
    uiState: SongPlayerUiState,
    onPlaySong: () -> Unit,
    onNextSong: () -> Unit,
    onPrevSong: () -> Unit,
    positionSliderChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
){
    val name by uiState.songName.collectAsState()
    val songImg by uiState.image.collectAsState()
    val playButton by uiState.playButton.collectAsState()
    val sliderPosition by uiState.sliderPosition.collectAsState()
    Column(
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        Image(
            painter = songImg?.let { rememberAsyncImagePainter(model = it) } ?: painterResource(
                id = R.drawable.motley
            ),
            contentDescription = stringResource(R.string.album_img),
            modifier = Modifier
                .size(width = 400.dp, height = 400.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name ?: stringResource(R.string.song_name) ,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 24.sp
            )
        Slider(
            value = sliderPosition,
            onValueChange = positionSliderChanged,
        )
        Row(
           horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp)
        ) {
            FloatingActionButton(
                onClick = onPrevSong,
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.prev),
                    contentDescription = stringResource(R.string.settings),
                    tint = MaterialTheme.colorScheme.onError
                )}
                Spacer(modifier = Modifier.weight(15f))
                FloatingActionButton(
                    onClick = onPlaySong,
                    containerColor = MaterialTheme.colorScheme.error
                ){
                Icon(
                    painter = painterResource(id = playButton),
                    contentDescription = stringResource(R.string.settings),
                    tint = MaterialTheme.colorScheme.onError
                )}
                Spacer(modifier = Modifier.weight(15f))
                FloatingActionButton(
                    onClick = onNextSong,
                    containerColor = MaterialTheme.colorScheme.error
                ){
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = stringResource(R.string.settings),
                    tint = MaterialTheme.colorScheme.onError
                )}
            }
        }
    }
