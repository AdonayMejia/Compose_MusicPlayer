package com.example.compose_music_player.model

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class SongModel(
    val name: String,
    val resource: Uri,
    val image: Uri,
    var selected: Boolean = false
) {
    companion object {
        fun create(//function to transform the data class type for the type require to use
            name: String,
            @RawRes songFile: Int,
            @DrawableRes songImageRes: Int,
        ): SongModel = SongModel(
            name = name,
            resource = Uri.parse("${SongProvider.UriPath}$songFile"),
            image = Uri.parse("${SongProvider.UriPath}$songImageRes")
        )
    }
}