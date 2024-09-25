package ru.vladsaybulin.feature.profile

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.model.user.UserImage

@Composable
fun ProfileRoute(viewModel: ProfileViewModel = hiltViewModel()) {
    val isMe by viewModel.isMe.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        isMe = isMe,
        onLogout = viewModel::logout,
        onLoginViaShikimori = viewModel::loginViaShikimori,
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    isMe: Boolean,
    onLoginViaShikimori: () -> Unit,
    onLogout: () -> Unit,
) {
    Box(modifier = Modifier.padding(LocalScreenContentPadding.current)) {
        ProfileContent(
            state = state,
            isMe = isMe,
            onLoginViaShikimori = onLoginViaShikimori,
            onLogout = onLogout,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    state: ProfileUiState,
    isMe: Boolean,
    onLoginViaShikimori: () -> Unit,
    onLogout: () -> Unit,
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val (showLogoutConfirmationDialog, setShowLogoutConfirmationDialog) = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                isMe = isMe,
                onLogoutClick = { setShowLogoutConfirmationDialog(true) },
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { scaffoldPaddings ->
        Column(
            modifier = Modifier
                .padding(scaffoldPaddings)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                when (state) {
                    ProfileUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    ProfileUiState.NotAuthorized -> NotAuthorizedBody(onLoginViaShikimori = onLoginViaShikimori)
                    is ProfileUiState.Success -> ProfileBody(state = state)
                }
            }

            if (isMe) {
                SeanimeLinks()
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
    isMe: Boolean,
    onLogoutClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { },
        actions = {
            if (isMe) {
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
private fun NotAuthorizedBody(onLoginViaShikimori: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(stringResource(id = R.string.feature_profile_not_authorized_headline))
        Text(stringResource(id = R.string.feature_profile_not_authorized_message))
        Button(onClick = onLoginViaShikimori) {
            Text(stringResource(id = R.string.feature_profile_not_authorized_login_with_shikimori))
        }
    }
}

@Composable
private fun ProfileBody(
    state: ProfileUiState.Success
) {
    Column {
        UserImage(state.user.image)
        Spacer(modifier = Modifier.height(16.dp))
        UserNickname(nickname = state.user.nickname)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val uriHandler = LocalUriHandler.current
            WorkInProgressProfileSection(openInBrowser = { uriHandler.openUri("$USER_URL/${state.user.nickname}") })
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
private fun WorkInProgressProfileSection(openInBrowser: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = SeanimeIcons.Construction,
            contentDescription = stringResource(id = R.string.feature_profile_work_in_progress_profile_section),
            modifier = Modifier.size(64.dp),
        )
        Text(
            text = stringResource(id = R.string.feature_profile_work_in_progress_profile_section),
            textAlign = TextAlign.Center
        )

        TextButton(onClick = openInBrowser) {
            Text(text = stringResource(id = R.string.feature_profile_open_in_browser))
        }
    }
}

@Composable
private fun SeanimeLinks() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val uriHandler = LocalUriHandler.current
        val context = LocalContext.current

        TextButton(onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) }) {
            Text(text = stringResource(R.string.feature_profile_privacy_policy))
        }

        TextButton(onClick = { context.startActivity(Intent(context, OssLicensesMenuActivity::class.java)) }) {
            Text(text = stringResource(R.string.feature_profile_open_source_licenses))
        }

        TextButton(onClick = { uriHandler.openUri(FEEDBACK_URL) }) {
            Text(text = stringResource(R.string.feature_profile_feedback))
        }
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

private const val PRIVACY_POLICY_URL = "" //TODO insert privacy policy url
private const val FEEDBACK_URL = "https://forms.yandex.ru/u/66ef341273cee76f30ff63e2/"

private const val USER_URL = "https://shikimori.one"