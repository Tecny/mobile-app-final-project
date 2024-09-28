package Beans

data class Usuarios(
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String,
    val roles: List<String>,
    val bankAccount: String,
    )