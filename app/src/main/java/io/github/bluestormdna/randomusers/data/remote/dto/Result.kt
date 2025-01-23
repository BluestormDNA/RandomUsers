package io.github.bluestormdna.randomusers.data.remote.dto


import io.github.bluestormdna.randomusers.data.local.entities.LocationEntity
import io.github.bluestormdna.randomusers.data.local.entities.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("cell")
    val cell: String,
    @SerialName("dob")
    val dob: Dob,
    @SerialName("email")
    val email: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Id,
    @SerialName("location")
    val location: Location,
    @SerialName("login")
    val login: Login,
    @SerialName("name")
    val name: Name,
    @SerialName("nat")
    val nat: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("picture")
    val picture: Picture,
    @SerialName("registered")
    val registered: Registered
)

fun Result.toEntity() = UserEntity(
    uuid = login.uuid,
    gender = gender,
    firstName = name.first,
    lastName = name.last,
    location = LocationEntity(
        street = location.street.name,
        city = location.city,
        state = location.state,
    ),
    registeredDate = registered.date,
    email = email,
    phone = phone,
    image = picture.large,
    thumbnail = picture.thumbnail,
    visible = true,
)