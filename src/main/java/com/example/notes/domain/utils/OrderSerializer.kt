package com.example.notes.domain.utils

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
object AppSettingsSerializer : Serializer<SortPreference> {
    override val defaultValue: SortPreference = SortPreference.DEFAULT

    override suspend fun readFrom(
        input: InputStream,
    ): SortPreference = withContext(Dispatchers.IO) {
        try {
            ProtoBuf.decodeFromByteArray<SortPreference>(input.readBytes())
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: SortPreference,
        output: OutputStream,
    ) = withContext(Dispatchers.IO) {
        try {
            output.write(
                ProtoBuf.encodeToByteArray(t)
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
        }

    }

}


