package ru.vladsaybulin.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.model.user.UserImage

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onLogout = { },
        onLoginWithShikimori = { }
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    onLoginWithShikimori: () -> Unit,
    onLogout: () -> Unit
) {
    Box(modifier = Modifier.padding(LocalScreenContentPadding.current)) {
        ProfileContent(
            state = state,
            onLoginWithShikimori = onLoginWithShikimori,
            onLogout = onLogout
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onLoginWithShikimori: () -> Unit,
    onLogout: () -> Unit,
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val (showLogoutConfirmationDialog, setShowLogoutConfirmationDialog) = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                isAuthorized = state != ProfileUiState.NotAuthorized,
                onLogoutClick = { setShowLogoutConfirmationDialog(true) },
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { scaffoldPaddings ->
        Box(
            modifier = Modifier
                .padding(scaffoldPaddings)
                .fillMaxSize()
        ) {
            when (state) {
                ProfileUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                ProfileUiState.NotAuthorized -> NotAuthorizedBody(onLoginWithShikimori = onLoginWithShikimori)
                is ProfileUiState.Success -> ProfileBody(state = state)
            }
        }
    }

    if (showLogoutConfirmationDialog) {
        LogoutConfirmationDialog(
            onConfirmed = {
                onLogout()
                setShowLogoutConfirmationDialog(false)
            },
            onDismissRequest = { setShowLogoutConfirmationDialog(false) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    isAuthorized: Boolean,
    onLogoutClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { },
        actions = {
            if (isAuthorized) {
                IconButton(onClick = onLogoutClick) {
                    Icon(
                        imageVector = SeanimeIcons.Logout,
                        contentDescription = stringResource(id = R.string.feature_profile_logout),
                        tint = SeanimeTheme.colorScheme.error
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun NotAuthorizedBody(onLoginWithShikimori: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(stringResource(id = R.string.feature_profile_not_authorized_headline))
        Text(stringResource(id = R.string.feature_profile_not_authorized_message))
        Button(onClick = onLoginWithShikimori) {
            Text(stringResource(id = R.string.feature_profile_not_authorized_login_with_shikimori))
        }
    }
}

@Composable
private fun ProfileBody(
    state: ProfileUiState.Success
) {
    LazyColumn {
        item {
            UserImage(state.user.image)
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            UserNickname(nickname = state.user.nickname)
        }
    }
}

@Composable
private fun UserImage(image: UserImage) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(
            shape = CircleShape,
            shadowElevation = 4.dp
        ) {
            AsyncImage(
                model = image.x160Url,
                contentDescription = null,
                modifier = Modifier.size(128.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun UserNickname(nickname: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(text = nickname, style = SeanimeTheme.typography.headlineSmall)
    }
}

@Composable
private fun LogoutConfirmationDialog(
    onConfirmed: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onConfirmed,
        confirmButton = {
            TextButton(onClick = onConfirmed) {
                Text(
                    text = stringResource(id = R.string.feature_profile_logout_confirm),
                    color = SeanimeTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.feature_profile_logout_dismiss))
            }
        },
        title = { Text(text = stringResource(id = R.string.feature_profile_logout_title)) },
        text = { Text(text = stringResource(id = R.string.feature_profile_logout_message)) }
    )
}