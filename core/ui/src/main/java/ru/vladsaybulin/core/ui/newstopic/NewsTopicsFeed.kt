package ru.vladsaybulin.core.ui.newstopic

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.model.user.BriefUser

fun LazyListScope.newsTopicsFeed(
    newsTopics: List<Topic>,
    key: ((Topic) -> Any)? = { it.id },
    onTopicClick: (Topic) -> Unit,
    onUserClick: (BriefUser) -> Unit
) {
    itemsIndexed(
        items = newsTopics,
        key = if (key != null) {
            { _, topic -> key(topic) }
        } else null
    ) { index, newsTopic ->
        NewsTopicCard(
            topic = newsTopic,
            onClick = { onTopicClick(newsTopic) },
            onUserClick = { onUserClick(newsTopic.user) },
            modifier = Modifier.animateItem()
        )
        if (index < newsTopics.size - 1) {
            HorizontalDivider()
        }
    }
}

fun LazyListScope.newsTopicFeed(
    newsTopics: LazyPagingItems<Topic>,
    onTopicClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit
) {
    items(
        count = newsTopics.itemCount,
        key = newsTopics.itemKey { it.id }
    ) { index ->
        val newsTopic = newsTopics[index]
        if (newsTopic != null) {
            NewsTopicCard(
                topic = newsTopic,
                onClick = { onTopicClick(newsTopic.id) },
                onUserClick = { onUserClick(newsTopic.user.id) }
            )
        }
    }
}