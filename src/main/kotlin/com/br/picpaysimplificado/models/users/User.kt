package com.br.picpaysimplificado.models.users

import com.br.picpaysimplificado.database.users.UserEntity
import kotlinx.serialization.Serializable

@Serializable
class User(
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    var balance: Double,
    val cpf: String,
    val email: String,
    val password: String,
    val userType: UserType
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id,
            firstName,
            lastName,
            balance,
            cpf,
            email,
            password,
            userType
        )
    }
}