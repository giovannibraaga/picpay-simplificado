package com.br.picpaysimplificado.models.users

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    var balance: Double,
    val cpf: String,
    val email: String,
    val password: String,
    val userType: UserType
)