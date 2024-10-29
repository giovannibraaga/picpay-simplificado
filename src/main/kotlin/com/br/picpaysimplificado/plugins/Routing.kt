package com.br.picpaysimplificado.plugins

import com.br.picpaysimplificado.database.transactions.TransactionsDAO
import com.br.picpaysimplificado.database.users.UserDAO
import com.br.picpaysimplificado.models.transactions.Transactions
import com.br.picpaysimplificado.models.users.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(userDAO: UserDAO, transactionsDAO: TransactionsDAO) {
    routing {
        route("/users") {
            get {
                try {
                    val list = userDAO.getList()
                    call.respond(HttpStatusCode.OK, list)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Error: ${e.message}")
                }
            }

            post {
                try {
                    val user = call.receive<User>()
                    userDAO.add(user)
                    call.respond(HttpStatusCode.Created, user)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Error: ${e.message}")
                }
            }
        }

        route("/transactions") {
            get {
                try {
                    val list = transactionsDAO.getList()
                    call.respond(HttpStatusCode.OK, list)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Error: ${e.message}")
                }
            }

            post {
                try {
                    val transactionRequest = call.receive<Transactions>()

                    val processedTransaction = transactionsDAO.processTransaction(transactionRequest)

                    call.respond(HttpStatusCode.Created, processedTransaction)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Erro: ${e.message}")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Erro interno: ${e.message}")
                }
            }
        }
    }
}
