package com.br.picpaysimplificado

import com.br.picpaysimplificado.plugins.configureDatabases
import com.br.picpaysimplificado.plugins.configureHTTP
import com.br.picpaysimplificado.plugins.configureRouting
import com.br.picpaysimplificado.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureRouting()
}
