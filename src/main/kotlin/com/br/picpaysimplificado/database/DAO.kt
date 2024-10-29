package com.br.picpaysimplificado.database

import jakarta.persistence.EntityManager

abstract class DAO<TModel, TEntity>(val manager: EntityManager, private val entityType: Class<TEntity>) {
    abstract fun toModel(entity: TEntity): TModel

    abstract fun toEntity(model: TModel): TEntity

    open fun getList(): List<TModel> {
        val query = manager.createQuery(
            "FROM ${entityType.simpleName}", entityType
        )

        return query.resultList.map { toModel(it) }
    }

    open fun add(model: TModel) {
        try {
            manager.transaction.begin()
            val entity = toEntity(model)
            manager.persist(entity)
            manager.transaction.commit()
        } catch (e: Exception) {
            if (manager.transaction.isActive) {
                manager.transaction.rollback()
            }
            throw e
        }
    }
}