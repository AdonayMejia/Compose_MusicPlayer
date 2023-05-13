package com.example.compose_music_player.ui

import android.view.View
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.compose_music_player.ui.theme.TestComposeTheme

inline fun Fragment.composeView(
    crossinline content: @Composable () -> Unit
): View {
    return ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            TestComposeTheme {
                content()
            }
        }
    }
}