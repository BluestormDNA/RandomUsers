package io.github.bluestormdna.randomusers.data.remote.api

import io.github.bluestormdna.randomusers.data.remote.dto.RandomUserApiResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DefaultRandomUserApi(
    private val httpClient: HttpClient
): RandomUserApi {
    override suspend fun getRandomUsers(
        page: Int,
        results: Int,
        seed: String
    ): RandomUserApiResponseDto {
        return httpClient.get(BASE_URL) {
            parameter("page", page)
            parameter("results", results)
            parameter("seed", seed)
        }.body()
    }

    companion object {
        const val BASE_URL = "https://randomuser.me/api/"
    }
}