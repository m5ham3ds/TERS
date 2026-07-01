package com.example.domain.usecase

import com.example.domain.model.AiModel
import com.example.domain.repository.ModelRepository
import kotlinx.coroutines.flow.Flow

class GetModelsUseCase(
    private val modelRepository: ModelRepository
) {
    operator fun invoke(): Flow<List<AiModel>> {
        return modelRepository.getModels()
    }
}
