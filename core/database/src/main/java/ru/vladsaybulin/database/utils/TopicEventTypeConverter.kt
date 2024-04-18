package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.topic.TopicEvent
import ru.vladsaybulin.model.topic.asTopicEvent

class TopicEventTypeConverter {

    @TypeConverter
    fun topicEventToString(value: TopicEvent) = value.serializedValue

    @TypeConverter
    fun stringToTopicEvent(value: String) = value.asTopicEvent()
}