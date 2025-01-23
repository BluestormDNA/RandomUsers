package io.github.bluestormdna.randomusers

import io.github.bluestormdna.randomusers.data.local.entities.LocationEntity
import io.github.bluestormdna.randomusers.data.local.entities.UserEntity

object UserFactory {
    fun generateEntity(size: Int) = List(size) { index ->
        UserEntity(
            uuid = "uuid $index",
            gender = "gender $index",
            firstName = "firstName $index",
            lastName = "lastName $index",
            location = LocationEntity(
                street = "street $index",
                city = "city $index",
                state = "state $index",
            ),
            registeredDate = "date $index",
            email = "email@$index.com",
            phone = "123 $index",
            image = "",
            visible = true,
            thumbnail = "",
        )
    }
}