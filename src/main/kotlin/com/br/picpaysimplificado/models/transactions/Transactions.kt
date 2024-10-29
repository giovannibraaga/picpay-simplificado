package com.br.picpaysimplificado.models.transactions

import kotlinx.serialization.Serializable

@Serializable
data class Transactions(
    val id: Long = 0,
    val value: Double,
    val payerId: Long,
    val payeeId: Long
)