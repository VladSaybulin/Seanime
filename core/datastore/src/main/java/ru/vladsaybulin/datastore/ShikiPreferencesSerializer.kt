package ru.vladsaybulin.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ru.vladsaybulin.core.datastore.proto.ShikiPreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class ShikiPreferencesSerializer @Inject constructor() : Serializer<ShikiPreferences> {

    override val defaultValue: ShikiPreferences = ShikiPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ShikiPreferences =
        try {
            // readFrom is already called on the data store background thread
            ShikiPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: ShikiPreferences, output: OutputStream) {
        // writeTo is already called on the data store background thread
        t.writeTo(output)
    }

}