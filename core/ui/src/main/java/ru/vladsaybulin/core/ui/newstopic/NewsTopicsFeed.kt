package ru.vladsaybulin.core.ui.newstopic

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.model.topic.Topic

fun LazyListScope.newsTopicsFeed(
    newsTopics: List<Topic>,
    onTopicClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit
) {
    itemsIndexed(
        items = newsTopics,
        key = { _, it -> it.id }
    ) { index, newsTopic ->
        NewsTopicCard(
            topic = newsTopic,
            onClick = { onTopicClick(newsTopic.id) },
            onUserClick = { onUserClick(newsTopic.user.id) }
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