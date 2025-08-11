package com.jawapbo.sportistic.core.data

import android.util.Log
import androidx.datastore.core.Serializer
import com.jawapbo.sportistic.shared.data.core.LocalDateTimeSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDateTime

object AuthDataStoreSerializer: Serializer<AuthDataStore> {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        serializersModule = SerializersModule {
            contextual(LocalDateTime::class, LocalDateTimeSerializer)
        }
    }
    override val defaultValue: AuthDataStore
        get() = AuthDataStore()

    override suspend fun readFrom(input: InputStream): AuthDataStore {
        return try {
            json.decodeFromString(
                deserializer = AuthDataStore.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Log.e("AuthTokenSerializer", "Error deserializing AuthTokens: ${e.message}")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: AuthDataStore,
        output: OutputStream
    ) {
        output.write(
            json.encodeToString(
                serializer = AuthDataStore.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }

}