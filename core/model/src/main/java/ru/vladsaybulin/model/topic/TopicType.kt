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