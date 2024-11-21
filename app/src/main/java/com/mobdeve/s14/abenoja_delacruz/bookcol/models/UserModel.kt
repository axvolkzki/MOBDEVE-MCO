package com.mobdeve.s14.abenoja_delacruz.bookcol.models

data class UserModel(
    val username: String,
    val email: String,
    val password: String,
) {
    init {
        require(username.isNotEmpty()) { "Username must not be empty" }
        require(email.isNotEmpty()) { "Email must not be empty" }
        require(password.isNotEmpty()) { "Password must not be empty" }

        require(username.length in 6..20) { "Username must be between 6 to 20 characters" }
        require(email.length <= 50) { "Email must not exceed 50 characters" }
        require(password.length in 8..20) { "Password must be between 8 to 20 characters" }

        require(username.matches(Regex("^[a-zA-Z0-9]*$"))) { "Username must only contain alphanumeric characters" }
        require(email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))) { "Email must be in the correct format" }
        require(password.matches(Regex("^[a-zA-Z0-9]*$"))) { "Password must only contain alphanumeric characters" }
    }
}
