package ru.vladsaybulin.core.ui2.entry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import ru.vladsaybulin.model.userrate.UserRateStatus

fun interface EntryItemColorsProducer {
    @Composable
    @ReadOnlyComposable
    operator fun invoke(userRateStatus: UserRateStatus): EntryItemColors

    object Surface : EntryItemColorsProducer {
        @Composable
        @ReadOnlyComposable
        override fun invoke(userRateStatus: UserRateStatus): EntryItemColors =
            EntryItemDefaults.SurfaceColors
    }

    object SurfaceContainer : EntryItemColorsProducer {
        @Composable
        @ReadOnlyComposable
        override fun invoke(userRateStatus: UserRateStatus): EntryItemColors =
            EntryItemDefaults.SurfaceContainerColors
    }

    object BasedOnUserRateStatus : EntryItemColorsProducer {
        @Composable
        @ReadOnlyComposable
        override fun invoke(userRateStatus: UserRateStatus): EntryItemColors =
            EntryItemDefaults.basedOnUserRateStatusColors(userRateStatus)

    }
}