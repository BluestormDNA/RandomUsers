package io.github.bluestormdna.randomusers.domain.model

data class User(
    val uuid: String,
    val gender: String,
    val firstName: String,
    val lastName: String,
    val location: Location,
    val registeredDate: String,
    val email: String,
    val phone: String,
    val thumbnail: String,
    val image: String,
) {
    companion object {
        val EMPTY: User = User(
            uuid = "",
            gender = "",
            firstName = "",
            lastName = "",
            location = Location("", "", ""),
            registeredDate = "",
            email = "",
            phone = "",
            thumbnail = "",
            image = "",
        )
    }
}