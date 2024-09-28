package Beans

data class LoginResponse(
    val id: Int,
    val username: String,
    val token: String
)