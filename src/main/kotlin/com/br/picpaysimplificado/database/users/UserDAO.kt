package com.br.picpaysimplificado.database.users

import com.br.picpaysimplificado.database.DAO
import com.br.picpaysimplificado.models.users.User
import jakarta.persistence.EntityManager

class UserDAO(manager: EntityManager) : DAO<User, UserEntity>(manager, UserEntity::class.java) {

    override fun toModel(entity: UserEntity): User {
        return User(
            entity.id,
            entity.firstName,
            entity.lastName,
            entity.balance,
            entity.cpf,
            entity.email,
            entity.password,
            entity.userType
        )
    }

    override fun toEntity(model: User): UserEntity {
        return UserEntity(
            model.id,
            model.firstName,
            model.lastName,
            model.balance,
            model.cpf,
            model.email,
            model.password,
            model.userType
        )
    }
}