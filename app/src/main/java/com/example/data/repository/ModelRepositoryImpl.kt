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
        // Implement model downloading via WorkManager
    }
}
