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

package ru.vladsaybulin.model.topic

enum class TopicType(val serializedValue: String) {
    Topic("Topic"),
    ClubUserTopic("Topics::ClubUserTopic"),
    EntryTopic("Topics::EntryTopic"),
    AnimeTopic("Topics::EntryTopics::AnimeTopic"),
    ArticleTopic("Topics::EntryTopics::ArticleTopic"),
    CharacterTopic("Topics::EntryTopics::CharacterTopic"),
    ClubPageTopic("Topics::EntryTopics::ClubPageTopic"),
    ClubTopic("Topics::EntryTopics::ClubTopic"),
    CollectionTopic("Topics::EntryTopics::CollectionTopic"),
    ContestTopic("Topics::EntryTopics::ContestTopic"),
    CosplayGalleryTopic("Topics::EntryTopics::CosplayGalleryTopic"),
    MangaTopic("Topics::EntryTopics::MangaTopic"),
    PersonTopic("Topics::EntryTopics::PersonTopic"),
    RanobeTopic("Topics::EntryTopics::RanobeTopic"),
    CritiqueTopic("Topics::EntryTopics::CritiqueTopic"),
    ReviewTopic("Topics::EntryTopics::ReviewTopic"),
    NewsTopic("Topics::NewsTopic"),
    ContestStatusTopic("Topics::NewsTopics::ContestStatusTopic"),
    None("")
}

fun String?.asTopicType() = when (this) {
    null -> TopicType.None
    else -> TopicType.entries.firstOrNull { it.serializedValue == this }
        ?: TopicType.None
}