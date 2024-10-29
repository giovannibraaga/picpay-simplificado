package com.example

import com.br.picpaysimplificado.database.DatabaseConnection
import com.br.picpaysimplificado.database.users.UserDAO
import com.br.picpaysimplificado.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        val entityManager = DatabaseConnection.getEntityManager()
        application {
            configureRouting(UserDAO(entityManager))
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
