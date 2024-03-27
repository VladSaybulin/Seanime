package ru.vladsaybulin.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class AuthPreferencesSerializer @Inject constructor() : Serializer<AuthPreferences> {
    override val defaultValue: AuthPreferences = AuthPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthPreferences =
        try {
            // readFrom is already called on the data store background thread
            AuthPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: AuthPreferences, output: OutputStream) {
        // writeTo is already called on the data store background thread
        t.writeTo(output)
    }
}