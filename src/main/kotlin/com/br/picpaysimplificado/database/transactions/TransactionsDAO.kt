package com.br.picpaysimplificado.database.transactions

import com.br.picpaysimplificado.database.DAO
import com.br.picpaysimplificado.database.users.UserEntity
import com.br.picpaysimplificado.models.transactions.Transactions
import com.br.picpaysimplificado.models.users.UserType
import io.ktor.client.*
import io.ktor.client.request.*
import jakarta.persistence.EntityManager

class TransactionsDAO(manager: EntityManager) :
    DAO<Transactions, TransactionsEntity>(manager, TransactionsEntity::class.java) {

    override fun toEntity(model: Transactions): TransactionsEntity {
        return TransactionsEntity(
            model.id,
            model.value,
            model.payerId,
            model.payeeId
        )
    }

    override fun toModel(entity: TransactionsEntity): Transactions {

        return Transactions(
            entity.id,
            entity.transactionValue,
            entity.payerId,
            entity.payeeId
        )
    }

    suspend fun processTransaction(transaction: Transactions): TransactionsEntity {
        val payer = manager.find(UserEntity::class.java, transaction.payerId)
            ?: throw IllegalArgumentException("Payer não encontrado")
        val payee = manager.find(UserEntity::class.java, transaction.payeeId)
            ?: throw IllegalArgumentException("Payee não encontrado")

        if (transaction.value <= 0.0) {
            throw IllegalArgumentException("O valor da transação deve ser maior do que R$0,00")
        }
        if (payer.userType == UserType.LOJISTA) {
            throw IllegalArgumentException("Lojistas não podem realizar transações")
        }
        if (payer.balance < transaction.value) {
            throw IllegalArgumentException("Saldo insuficiente")
        }
        if (payer.id == payee.id) {
            throw IllegalArgumentException("Você não pode transferir dinheiro para si mesmo")
        }
        try {
            payer.balance -= transaction.value
            payee.balance += transaction.value

            manager.transaction.begin()
            manager.merge(payer)
            manager.merge(payee)
            val transactionEntity = TransactionsEntity(
                transactionValue = transaction.value,
                payerId = payer.id,
                payeeId = payee.id
            )
            manager.persist(transactionEntity)
            manager.transaction.commit()

            val client = HttpClient()

            notifyUser(client)

            return transactionEntity
        } catch (e: Exception) {
            if (manager.transaction.isActive) {
                manager.transaction.rollback()
            }
            throw e
        }
    }

    suspend fun notifyUser(client: HttpClient) {
        val notiURL = "https://util.devi.tools/api/v1/notify"

        client.post(notiURL)
    }
}
