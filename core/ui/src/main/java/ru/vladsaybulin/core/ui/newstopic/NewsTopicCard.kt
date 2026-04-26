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

package ru.vladsaybulin.core.ui.newstopic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import org.primeframework.transformer.domain.Node
import org.primeframework.transformer.domain.TagNode
import org.primeframework.transformer.service.HTMLParser
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.LocalTimeZone
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.model.user.BriefUser
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun NewsTopicCard(
    topic: Topic,
    onClick: () -> Unit,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val headerImage = remember(topic.id) { getHeaderImage(topic) }

    Surface(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            if (headerImage != null) {
                NewsResourceHeaderImage(headerImage)
            }

            Spacer(modifier = Modifier.height(16.dp))

            NewsResourceTitle(topicTitle = topic.title)
            Spacer(modifier = Modifier.height(4.dp))
            NewsResourceMetaData(
                user = topic.user,
                createdAt = topic.createdAt,
                onUserClick = onUserClick
            )
        }
    }
}

@Composable
fun NewsResourceHeaderImage(headerImage: NewsTopicHeaderImage) {
    val isLocalInspection = LocalInspectionMode.current

    val imageLoader = rememberAsyncImagePainter(
        model = headerImage.url,
        contentScale = ContentScale.Crop
    )

    Image(
        painter = if (!isLocalInspection) {
            imageLoader
        } else {
            painterResource(id = R.drawable.preview_poster_1)
        },
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
            .clip(RoundedCornerShape(16.dp)),
        contentDescription = null,
        alignment = headerImage.alignment
    )
}

@Composable
fun NewsResourceTitle(
    topicTitle: String
) {
    Text(topicTitle, style = SeanimeTheme.typography.titleLarge)
}

@Composable
fun dateFormatted(publishDate: Instant): String = DateTimeFormatter
    .ofLocalizedDate(FormatStyle.MEDIUM)
    .withLocale(Locale.getDefault())
    .withZone(LocalTimeZone.current.toJavaZoneId())
    .format(publishDate.toJavaInstant())

@Composable
fun NewsResourceMetaData(
    user: BriefUser,
    createdAt: Instant,
    onUserClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        NewsResourceAuthor(
            user = user,
            onUserClick = onUserClick
        )
        Text(text = " • ")
        Text(
            text = dateFormatted(publishDate = createdAt),
            style = SeanimeTheme.typography.labelSmall,
        )
    }
}

@Composable
fun NewsResourceAuthor(
    user: BriefUser,
    onUserClick: () -> Unit,
) {
    Surface(
        shape = CircleShape,
        onClick = onUserClick
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NewsResourceAuthorAvatar(avatarUrl = user.avatarUrl)
            Spacer(modifier = Modifier.width(8.dp))
            NewsResourceAuthorNickname(userNickname = user.nickname)
        }
    }
}

@Composable
fun NewsResourceAuthorAvatar(
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
            .size(24.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
fun NewsResourceAuthorNickname(
    userNickname: String
) {
    Text(userNickname, style = SeanimeTheme.typography.bodyMedium)
}

fun getHeaderImage(topic: Topic): NewsTopicHeaderImage? {
    val doc = HTMLParser().buildDocument(topic.footerHtml, emptyMap())

    for (node in doc.children) {
        val imageUrl = findFirstImageUrl(node)
        if (imageUrl != null) return imageUrl
    }

    return null
}

fun findFirstImageUrl(node: Node): NewsTopicHeaderImage? {
    if (node is TagNode) {
        var imageUrl = getImageTagIfImgTag(node)
        if (imageUrl != null) return imageUrl
        for (childNode in node.children) {
            imageUrl = findFirstImageUrl(childNode)
            if (imageUrl != null) return imageUrl
        }
        return null
    } else return null
}

fun getImageTagIfImgTag(tagNode: TagNode): NewsTopicHeaderImage? {
    if (tagNode.name != "img") return null
    val imageUrl = tagNode.attributes["src"] ?: return null
    val url = if (!imageUrl.startsWith("http")) "https:$imageUrl" else imageUrl
    val parentTagClass = tagNode.parent?.attributes?.get("class")
    return if (parentTagClass == "video-link") {
        NewsTopicHeaderImage(url, Alignment.Center)
    } else {
        NewsTopicHeaderImage(url, Alignment.TopCenter)
    }
}

data class NewsTopicHeaderImage(
    val url: String,
    val alignment: Alignment
)
