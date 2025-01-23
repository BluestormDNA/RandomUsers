package io.github.bluestormdna.randomusers.data.remote.api

import io.github.bluestormdna.randomusers.data.remote.dto.RandomUserApiResponseDto

interface RandomUserApi {
    suspend fun getRandomUsers(page: Int, results: Int, seed: String) : RandomUserApiResponseDto
}