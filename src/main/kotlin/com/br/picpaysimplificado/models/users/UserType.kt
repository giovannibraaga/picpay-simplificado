package com.br.picpaysimplificado.models.users

import kotlinx.serialization.Serializable

@Serializable
enum class UserType {
    COMUM, LOJISTA
}