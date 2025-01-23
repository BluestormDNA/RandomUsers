package io.github.bluestormdna.randomusers.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.bluestormdna.randomusers.domain.model.Location
import io.github.bluestormdna.randomusers.domain.model.User

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    val uuid: String,
    val gender: String,
    val firstName: String,
    val lastName: String,
    @Embedded
    val location: LocationEntity,
    val registeredDate: String,
    val email: String,
    val phone: String,
    val image: String,
    val thumbnail: String,
    val visible: Boolean,
) {
    fun toDomain() = User(
        uuid = uuid,
        gender = gender,
        firstName = firstName,
        lastName = lastName,
        location = Location(
            street = location.street,
            city = location.city,
            state = location.state,
        ),
        registeredDate = registeredDate,
        email = email,
        phone = phone,
        image = image,
        thumbnail = thumbnail
    )
}