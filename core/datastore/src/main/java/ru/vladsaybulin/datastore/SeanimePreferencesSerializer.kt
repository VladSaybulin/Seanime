package ru.vladsaybulin.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ru.vladsaybulin.core.datastore.proto.SeanimePreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class SeanimePreferencesSerializer @Inject constructor() : Serializer<SeanimePreferences> {

    override val defaultValue: ShikiPreferences = ShikiPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SeanimePreferences =
        try {
            // readFrom is already called on the data store background thread
            SeanimePreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: SeanimePreferences, output: OutputStream) {
        // writeTo is already called on the data store background thread
        t.writeTo(output)
    }

}