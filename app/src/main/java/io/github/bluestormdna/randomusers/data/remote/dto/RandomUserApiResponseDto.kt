package io.github.bluestormdna.randomusers.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RandomUserApiResponseDto(
    @SerialName("info")
    val info: Info,
    @SerialName("results")
    val results: List<Result>
)