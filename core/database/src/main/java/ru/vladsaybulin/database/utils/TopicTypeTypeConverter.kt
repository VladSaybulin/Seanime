package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.topic.TopicType
import ru.vladsaybulin.model.topic.asTopicType

class TopicTypeTypeConverter {
    @TypeConverter
    fun topicTypeToString(value: TopicType) = value.serializedValue

    @TypeConverter
    fun stringToTopicType(value: String) = value.asTopicType()
}