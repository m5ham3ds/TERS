package com.example.domain.repository

import com.example.domain.model.AiModel
import com.example.domain.model.Provider
import kotlinx.coroutines.flow.Flow

interface ProviderRepository {
    suspend fun getProviders(): List<Provider>
    suspend fun validateConnection(providerId: String): Boolean
    suspend fun sendRequest(providerId: String, prompt: String): String
}

interface ModelRepository {
    fun getModels(): Flow<List<AiModel>>
    suspend fun downloadModel(model: AiModel)
    suspend fun deleteModel(model: AiModel)
}
