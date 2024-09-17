package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.strings.LocalTitleStrings
import ru.vladsaybulin.feature.title.details.R
import ru.vladsaybulin.model.common.EntryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequireAuthDialog(
    authWithShikimori: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        content = {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = SeanimeTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(Modifier.padding(24.dp)) {

                    Text(
                        text = stringResource(id = R.string.feature_title_details_require_auth_title),
                        style = SeanimeTheme.typography.headlineSmall,
                        color = SeanimeTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val titleName = stringResource(
                        id =
                        when (LocalTitleStrings.current) {
                            EntryType.Anime -> R.string.feature_title_details_require_auth_anime
                            else -> R.string.feature_title_details_require_auth_manga
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.feature_title_details_require_auth_message, titleName),
                        style = SeanimeTheme.typography.bodyMedium,
                        color = SeanimeTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismissRequest) {
                            Text(
                                text = stringResource(id = R.string.feature_title_details_require_close),
                                color = SeanimeTheme.colorScheme.secondary
                            )
                        }

                        TextButton(
                            onClick = authWithShikimori,
                            modifier = Modifier
                        ) {
                            Text(
                                text = stringResource(id = R.string.feature_title_details_require_auth_with_shikimori),
                                color = SeanimeTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun RequireAuthDialogPreview() {
    RequireAuthDialog(
        authWithShikimori = {},
        onDismissRequest = {}
    )
}