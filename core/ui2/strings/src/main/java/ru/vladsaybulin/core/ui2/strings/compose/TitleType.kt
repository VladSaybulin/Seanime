package ru.vladsaybulin.core.ui2.strings.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui2.strings.AppStrings
import ru.vladsaybulin.model.common.EntryType

@Composable
fun EntryType.asString() = stringResource(AppStrings.titleType(this))