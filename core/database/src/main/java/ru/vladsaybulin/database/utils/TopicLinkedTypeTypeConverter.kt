package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.model.topic.asTopicLinkedType

class TopicLinkedTypeTypeConverter {

    @TypeConverter
    fun topicLinkedTypeToString(value: TopicLinkedType) = value.serializedValue

    @TypeConverter
    fun stringToTopicLinkedType(value: String) = value.asTopicLinkedType()

}