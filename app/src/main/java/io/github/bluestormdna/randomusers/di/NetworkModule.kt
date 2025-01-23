package io.github.bluestormdna.randomusers.di

import io.github.bluestormdna.randomusers.data.remote.api.DefaultRandomUserApi
import io.github.bluestormdna.randomusers.data.remote.api.RandomUserApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::DefaultRandomUserApi) { bind<RandomUserApi>() }
    singleOf(::provideJsonConverter)
    singleOf(::provideNetworkClient)
}

private fun provideNetworkClient(
    converter: Json,
): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(converter)
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
    }
}

private fun provideJsonConverter(): Json {
    return Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }
}