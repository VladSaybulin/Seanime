package ru.vladsaybulin.database.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import ru.vladsaybulin.database.models.stats.StatsProto
import ru.vladsaybulin.model.userrate.UserRateStatus
import java.util.Base64
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
@ProvidedTypeConverter
class StatsTypeConverter @Inject constructor(private val protoBuf: ProtoBuf) {

    @TypeConverter
    fun statsOfIntToString(stats: StatsProto<Int>): String = encode(stats)

    @TypeConverter
    fun stringToStatsOfInt(statsStr: String): StatsProto<Int> = decode(statsStr)

    @TypeConverter
    fun statsOfUserRateStatusToString(stats: StatsProto<UserRateStatus>): String = encode(stats)

    @TypeConverter
    fun stringToStatsOfUserRateStatus(statsStr: String): StatsProto<UserRateStatus> = decode(statsStr)

    private inline fun <reified V> encode(stats: StatsProto<V>): String =
        if (stats.items.isNotEmpty()) {
            Base64.getEncoder().encodeToString(protoBuf.encodeToByteArray(stats))
        } else ""

    private inline fun <reified V> decode(statsStr: String): StatsProto<V> =
        if (statsStr.isNotEmpty()) {
            protoBuf.decodeFromByteArray<StatsProto<V>>(Base64.getDecoder().decode(statsStr))
        } else StatsProto(emptyList())
}