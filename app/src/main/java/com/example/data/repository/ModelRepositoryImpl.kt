package com.example.data.repository

import com.example.data.local.ModelDao
import com.example.domain.model.AiModel
import com.example.domain.repository.ModelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ModelRepositoryImpl(
    private val modelDao: ModelDao
) : ModelRepository {
    override fun getModels(): Flow<List<AiModel>> {
        return modelDao.getAllModels().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun downloadModel(model: AiModel) {
        val entity = com.example.data.local.AiModelEntity(
            id = model.id,
            name = model.name,
            providerId = model.providerId,
            version = model.version,
            capacity = model.capacity,
            format = model.format.name,
            isLocal = model.isLocal
        )
        modelDao.insertModels(listOf(entity))
    }

    override suspend fun deleteModel(model: AiModel) {
        val entity = com.example.data.local.AiModelEntity(
            id = model.id,
            name = model.name,
            providerId = model.providerId,
            version = model.version,
            capacity = model.capacity,
            format = model.format.name,
            isLocal = model.isLocal
        )
        modelDao.deleteModel(entity)
    }
}
