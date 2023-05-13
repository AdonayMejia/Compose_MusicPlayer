package com.example.compose_music_player.ui.songsettingview.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingView(){
    Scaffold() {
        BodyContent()
    }
}
@Composable
fun BodyContent(){
    Column() {
        Button(onClick = {/*TODO*/}){
            Text(text = "Navigate to List")
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShowButton(){
    SettingView()
}
