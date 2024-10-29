package com.br.picpaysimplificado.database.transactions

import com.br.picpaysimplificado.database.DAO
import com.br.picpaysimplificado.database.users.UserEntity
import com.br.picpaysimplificado.models.transactions.Transactions
import com.br.picpaysimplificado.models.users.User
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

    fun processTransaction(transaction: Transactions): TransactionsEntity {
        val payer = manager.find(UserEntity::class.java, transaction.payerId)
            ?: throw IllegalArgumentException("Payer não encontrado")
        val payee = manager.find(UserEntity::class.java, transaction.payeeId)
            ?: throw IllegalArgumentException("Payee não encontrado")

        if (transaction.value <= 0.0) {
            throw IllegalArgumentException("O valor da transação deve ser maior do que R$0,00")
        }
        if (payer.userType.equals("LOJISTA")) {
            throw IllegalArgumentException("Lojistas não podem realizar transações")
        }
        if (payer.balance < transaction.value) {
            throw IllegalArgumentException("Saldo insuficiente")
        }

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

        return transactionEntity
    }
}
