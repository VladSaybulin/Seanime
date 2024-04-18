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
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
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
    val headerImageUrl = headerImageUrl(topic)

    Surface(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            if (headerImageUrl != null) {
                NewsResourceHeaderImage(headerImageUrl = headerImageUrl)
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
fun NewsResourceHeaderImage(
    headerImageUrl: String
) {
    val isLocalInspection = LocalInspectionMode.current

    val imageLoader = rememberAsyncImagePainter(
        model = headerImageUrl
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
        contentDescription = null
    )
}

@Composable
fun NewsResourceTitle(
    topicTitle: String
) {
    Text(topicTitle, style = ShikimoriTheme.typography.titleLarge)
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
            style = ShikimoriTheme.typography.labelSmall,
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
    Text(userNickname, style = ShikimoriTheme.typography.bodyMedium)
}

fun headerImageUrl(topic: Topic): String? {
    val doc = HTMLParser().buildDocument(topic.footerHtml, emptyMap())

    for (node in doc.children) {
        val imageUrl = findFirstImageUrl(node)
        if (imageUrl != null) return imageUrl
    }

    return null
}

fun findFirstImageUrl(node: Node): String? {
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

fun getImageTagIfImgTag(tagNode: TagNode): String? {
    if (tagNode.name != "img") return null
    val imageUrl = tagNode.attributes["src"] ?: return null
    return if (!imageUrl.startsWith("http")) {
        "https:$imageUrl"
    } else {
        imageUrl
    }
}
