package io.github.bluestormdna.randomusers

import io.github.bluestormdna.randomusers.domain.model.Location
import io.github.bluestormdna.randomusers.domain.model.User

object UserFactory {
    fun generate(size: Int) = List(size) { index ->
        User(
            uuid = "uuid $index",
            gender = "gender $index",
            firstName = "firstName $index",
            lastName = "lastName $index",
            location = Location(
                street = "street $index",
                city = "city $index",
                state = "state $index",
            ),
            registeredDate = "date $index",
            email = "email@$index.com",
            phone = "123 $index",
            image = "",
            thumbnail = "",
        )
    }
}