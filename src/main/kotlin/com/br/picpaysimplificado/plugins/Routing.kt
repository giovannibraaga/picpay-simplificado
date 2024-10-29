package com.br.picpaysimplificado.plugins

import com.br.picpaysimplificado.database.transactions.TransactionsDAO
import com.br.picpaysimplificado.database.users.UserDAO
import com.br.picpaysimplificado.models.transactions.Transactions
import com.br.picpaysimplificado.models.users.User
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(userDAO: UserDAO, transactionsDAO: TransactionsDAO) {
    val client = HttpClient()

    routing {
        route("/users") {
            get {
                try {
                    val userList = userDAO.getList()
                    call.respond(HttpStatusCode.OK, userList)
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

        route("/transfer") {
            get {
                try {
                    val transactionList = transactionsDAO.getList()
                    call.respond(HttpStatusCode.OK, transactionList)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Error: ${e.message}")
                }
            }

            post {
                try {
                    val transactionRequest = call.receive<Transactions>()
                    val isAuthorized = transactionRequest.authTransaction(client)

                    if (!isAuthorized) {
                        call.respond(HttpStatusCode.Forbidden, "Transação não autorizada pelo serviço externo")
                        return@post
                    }

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
