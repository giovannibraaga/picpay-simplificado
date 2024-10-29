package com.br.picpaysimplificado.models.transactions

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
class Transactions(
    val id: Long = 0,
    val value: Double,
    val payerId: Long,
    val payeeId: Long
) {
    suspend fun authTransaction(client: HttpClient): Boolean {
        val url = "https://util.devi.tools/api/v2/authorize"
        val response: HttpResponse = client.get(url)

        if (response.status == HttpStatusCode.OK) {
            val jsonString = response.bodyAsText()
            val json = Json.parseToJsonElement(jsonString).jsonObject
            return json["data"]?.jsonObject?.get("authorization")?.jsonPrimitive?.booleanOrNull ?: false
        } else {
            return false
        }
    }


}