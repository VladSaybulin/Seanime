package ru.vladsaybulin.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.colors.onUserRateStatusContainerColor
import ru.vladsaybulin.core.ui.colors.userRateStatusContainerColor
import ru.vladsaybulin.core.ui.strings.animeUserRateStatusString
import ru.vladsaybulin.core.ui.userRateStatusIcon
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRateStatus

@Composable
fun DetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    onEntryClick: (EntryType, Long) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {},
    onUserRateClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailsScreen(
        uiState = uiState,
        onRetry = viewModel::onRetry,
        onRefresh = viewModel::onRefresh,
        onEntryClick = onEntryClick,
        onBackClick = onBackClick,
        modifier = modifier,
        onUserRateClick = onUserRateClick
    )
}

@Composable
fun DetailsScreen(
    uiState: DetailsUiState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    onRefresh: suspend () -> Unit,
    onEntryClick: (EntryType, Long) -> Unit,
    onUserRateClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    when (uiState) {
        is DetailsUiState.Error -> DetailsError(
            errorState = uiState,
            modifier = modifier,
            onRetry = onRetry
        )

        DetailsUiState.Loading -> DetailsLoading()

        is DetailsUiState.Success -> DetailsContent(
            state = uiState,
            onEntryClick = onEntryClick,
            onRefresh = onRefresh,
            onBackClick = onBackClick,
            modifier = modifier,
            onUserRateClick = onUserRateClick
        )
    }
}

@Composable
private fun DetailsError(
    errorState: DetailsUiState.Error,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_message_title),
            style = ShikimoriTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = R.string.error_message),
            style = ShikimoriTheme.typography.bodyMedium
        )
        errorState.throwable.message?.let {
            Text(
                text = it,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                style = ShikimoriTheme.typography.bodySmall
            )
        }
        Button(onClick = onRetry) {
            Text(text = "Повторить")
        }
    }
}

@Composable
private fun DetailsLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsContent(
    state: DetailsUiState.Success,
    onEntryClick: (EntryType, Long) -> Unit,
    onRefresh: suspend () -> Unit,
    onUserRateClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier
) {
    val listState = rememberLazyListState()
    val visibleTopBar by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 10
        }
    }

    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset <= 10
        }
    }

    var expandedDescription by remember { mutableStateOf(false) }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            DetailsTopBar(
                visibleTopBar = visibleTopBar,
                title = state.header.run { russianName ?: name },
                onBackClick = onBackClick,
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        floatingActionButton = {
            UserRateFab(
                status = UserRateStatus.Watching,
                expanded = expandedFab,
                onClick = onUserRateClick
            )
        }
    ) { scaffoldPadding ->
        LazyColumn(
            state = listState,
        ) {
            detailsHeaderItem(
                header = state.header,
                topSpacing = scaffoldPadding.calculateTopPadding()
            )
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            state.info.forEach { info ->
                if (!info.shouldShow) return@forEach
                item(key = info.key) {
                    DetailsInfoLine(
                        info = info,
                        modifier = HorizontalPaddingModifier
                    )
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
            }

            state.description?.let {
                item(key = "description_formatted") {
                    DetailsDescriptionContent(
                        description = it,
                        expanded = expandedDescription,
                        onExpandedChange = { expandedDescription = it },
                        modifier = HorizontalPaddingModifier
                    )
                }
            }

            state.authors?.let {
                item(key = "authors") {
                    AuthorsCarousel(
                        authors = it,
                        horizontalContentPadding = HorizontalPadding
                    )
                }
            }

            item {
                Box(
                    Modifier
                        .height(1500.dp)
                        .fillMaxWidth()


                )
            }
        }
    }
}

@Composable
private fun UserRateFab(
    status: UserRateStatus?,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = status?.let { userRateStatusIcon(userRateStatus = it) }
        ?: ShikimoriIcons.Add

    val text = status?.let { animeUserRateStatusString(userRateStatus = it) }
        ?: stringResource(id = R.string.add)

    ExtendedFloatingActionButton(
        text = { Text(text = text) },
        icon = { Icon(imageVector = icon, contentDescription = null) },
        onClick = onClick,
        expanded = expanded,
        containerColor = status?.let {
            userRateStatusContainerColor(userRateStatus = it)
        } ?: ShikimoriTheme.colorScheme.primaryContainer,
        contentColor = status?.let {
            onUserRateStatusContainerColor(userRateStatus = it)
        } ?: ShikimoriTheme.colorScheme.onPrimaryContainer,
        modifier = modifier
    )
}

private val HorizontalPadding = PaddingValues(horizontal = 16.dp)
private val HorizontalPaddingModifier = Modifier.padding(HorizontalPadding)

