package com.example.compose_music_player.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    secondary = secondaryDark,
    tertiary = tertiaryDark,
    onPrimaryContainer = onPrimaryDarkContainer,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryDarkContainer,
    onSecondaryContainer = onSecondaryDarkContainer,
    onTertiary = onTertiaryDark,
    tertiaryContainer = TertiaryDarkContainer,
    onTertiaryContainer = onTertiaryDarkContainer,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorDarkContainer,
    onErrorContainer = onErrorDarkContainer,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceTint = surfaceTintDark,
    outlineVariant = outlineVariantDark,
    scrim = scrim
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    secondary = secondaryLight,
    tertiary = tertiaryLight,
    onPrimaryContainer = onPrimaryLightContainer,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryLightContainer,
    onSecondaryContainer = onSecondaryLightContainer,
    onTertiary = onTertiaryLight,
    tertiaryContainer = TertiaryLightContainer,
    onTertiaryContainer = onTertiaryLightContainer,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorLightContainer,
    onErrorContainer = onErrorLightContainer,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceTint = surfaceTintLight,
    outlineVariant = outlineVariantLight,
    scrim = scrim
)

@Composable
fun TestComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
      SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.primary.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
      }
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}
