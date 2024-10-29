package com.br.picpaysimplificado.database.users

import com.br.picpaysimplificado.models.users.User
import com.br.picpaysimplificado.models.users.UserType
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "first_name")
    val firstName: String = "",

    @Column(name = "last_name")
    val lastName: String = "",

    @Column(name = "balance")
    val balance: Double = 0.0,

    @Column(name = "cpf", unique = true)
    val cpf: String = "",

    @Column(name = "email", unique = true)
    val email: String = "",

    @Column(name = "password")
    val password: String = "",

    @Column(name = "user_type")
    val userType: UserType = UserType.COMUM
) {

    fun toModel(): User {
        return User(
            this.id,
            this.firstName,
            this.lastName,
            this.balance,
            this.cpf,
            this.email,
            this.password,
            this.userType
        )
    }
}