package com.br.picpaysimplificado

import com.br.picpaysimplificado.database.DatabaseConnection
import com.br.picpaysimplificado.database.transactions.TransactionsDAO
import com.br.picpaysimplificado.database.users.UserDAO
import com.br.picpaysimplificado.plugins.configureHTTP
import com.br.picpaysimplificado.plugins.configureRouting
import com.br.picpaysimplificado.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val entityManager = DatabaseConnection.getEntityManager()
    val userDAO = UserDAO(entityManager)
    val transactionsDAO = TransactionsDAO(entityManager)

    configureSerialization()
    configureHTTP()
    configureRouting(userDAO, transactionsDAO)
}
