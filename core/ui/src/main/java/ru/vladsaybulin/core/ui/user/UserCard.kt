package ru.vladsaybulin.core.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.user.BriefUser

@Composable
fun UserCard(
    user: BriefUser,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(avatarUrl = user.avatarUrl)
            Spacer(modifier = Modifier.width(8.dp))
            UserNickname(userNickname = user.nickname)
        }
    }
}

@Composable
fun UserAvatar(
    avatarUrl: String
) {
    val imageLoader = rememberAsyncImagePainter(model = avatarUrl)

    val isInspectionMode = LocalInspectionMode.current

    Image(
        painter = if (!isInspectionMode) {
            imageLoader
        } else {
            painterResource(id = R.drawable.preview_poster_1)
        },
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
fun UserNickname(
    userNickname: String
) {
    Text(userNickname, style = ShikimoriTheme.typography.titleMedium)
}

@Preview
@Composable
fun UserCardPreview(@PreviewParameter(UserPreviewParameterProvider::class) user: BriefUser) {
    ShikimoriTheme {
        UserCard(user = user, onClick = { /*TODO*/ })
    }
}