/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
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
    Text(userNickname, style = SeanimeTheme.typography.titleMedium)
}

@Preview
@Composable
fun UserCardPreview(@PreviewParameter(UserPreviewParameterProvider::class) user: BriefUser) {
    SeanimeTheme {
        UserCard(user = user, onClick = { /*TODO*/ })
    }
}