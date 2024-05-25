package ru.vladsaybulin.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val LightDefaultColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Cyan40,
    onTertiary = Color.White,
    tertiaryContainer = Cyan90,
    onTertiaryContainer = Cyan10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    inverseSurface = DarkPurpleGray20,
    inverseOnSurface = DarkPurpleGray95,
    outline = PurpleGray50
)

val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Cyan80,
    onTertiary = Cyan20,
    tertiaryContainer = Cyan30,
    onTertiaryContainer = Cyan90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    inverseSurface = DarkPurpleGray90,
    inverseOnSurface = DarkPurpleGray10,
    outline = PurpleGray60
)

val LightRateStatusColors = UserRateColors(
    planned = Orange40,
    onPlanned = White,
    plannedContainer = Orange90,
    onPlannedContainer = Orange10,
    watching = Blue40,
    onWatching = White,
    watchingContainer = Blue90,
    onWatchingContainer = Blue10,
    completed = Green40,
    onCompleted = White,
    completedContainer = Green95,
    onCompletedContainer = Green10,
    onHold = Violet40,
    onOnHold = White,
    onHoldContainer = Violet90,
    onOnHoldContainer = Violet10,
    dropped = Red40,
    onDropped = White,
    droppedContainer = Red90,
    onDroppedContainer = Red10,
)

val DarkRateStatusColors = UserRateColors(
    planned = Orange80,
    onPlanned = Orange20,
    plannedContainer = Orange30,
    onPlannedContainer = Orange90,
    watching = Blue80,
    onWatching = Blue20,
    watchingContainer = Blue30,
    onWatchingContainer = Blue90,
    completed = Green80,
    onCompleted = Green20,
    completedContainer = Green30,
    onCompletedContainer = Green90,
    onHold = Violet80,
    onOnHold = Violet20,
    onHoldContainer = Violet30,
    onOnHoldContainer = Violet90,
    dropped = Red80,
    onDropped = Red20,
    droppedContainer = Red30,
    onDroppedContainer = Red90,
)

val LightEntryStatusColors = EntryStatusColors(
    anons = Orange40,
    ongoing = Blue40,
    released = Green40,
    paused = Violet40,
    discontinued = Red40,
)

val DarkEntryStatusColor = EntryStatusColors(
    anons = Orange80,
    ongoing = Blue80,
    released = Green80,
    paused = Violet80,
    discontinued = Red80,
)

@Composable
fun SeanimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkDefaultColorScheme
        else -> LightDefaultColorScheme
    }

    val userRateColors = if (darkTheme) DarkRateStatusColors else LightRateStatusColors
    val entryStatusColors = if (darkTheme) DarkEntryStatusColor else LightEntryStatusColors

    CompositionLocalProvider(
        LocalUserRateColors provides userRateColors,
        LocalEntryStatusColors provides entryStatusColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = shikimoriTypography(),
            content = content
        )
    }
}

object SeanimeTheme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val userRateColors: UserRateColors
        @Composable
        @ReadOnlyComposable
        get() = LocalUserRateColors.current

    val entryStatusColors: EntryStatusColors
        @Composable
        @ReadOnlyComposable
        get() = LocalEntryStatusColors.current
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S