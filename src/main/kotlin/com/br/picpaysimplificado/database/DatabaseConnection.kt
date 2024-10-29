package com.br.picpaysimplificado.database

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence

object DatabaseConnection {
    private val factory: EntityManagerFactory = Persistence.createEntityManagerFactory("picpaysimplificado")

    fun getEntityManager(): EntityManager {
        return factory.createEntityManager()
    }

    fun closeFactory() {
        factory.close()
    }
}