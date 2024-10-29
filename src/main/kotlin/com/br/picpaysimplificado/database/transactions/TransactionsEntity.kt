package com.br.picpaysimplificado.database.transactions

import jakarta.persistence.*
import kotlinx.serialization.Serializable

@Serializable
@Entity
@Table(name = "transactions")
class TransactionsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "transaction_value")
    val transactionValue: Double = 0.0,

    @Column(name = "payer_id")
    val payerId: Long = 0,

    @Column(name = "payee_id")
    val payeeId: Long = 0
)
