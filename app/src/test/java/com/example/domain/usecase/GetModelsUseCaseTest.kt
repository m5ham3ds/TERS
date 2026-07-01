package com.example.domain.usecase

import com.example.domain.model.AiModel
import com.example.domain.model.ModelFormat
import com.example.domain.repository.ModelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetModelsUseCaseTest {

    @Test
    fun `invoke should return models from repository`() = runTest {
        // Arrange
        val fakeModels = listOf(
            AiModel("1", "GPT-4", "openai", "4.0", "Cloud", ModelFormat.CLOUD, false),
            AiModel("2", "Llama-3", "meta", "3.0", "8B", ModelFormat.GGUF, true)
        )
        
        val fakeRepository = object : ModelRepository {
            override fun getModels(): Flow<List<AiModel>> = flowOf(fakeModels)
            override suspend fun downloadModel(model: AiModel) {}
            override suspend fun deleteModel(model: AiModel) {}
        }
        
        val useCase = GetModelsUseCase(fakeRepository)

        // Act
        val result = useCase()
        
        // Assert
        result.collect { models ->
            assertEquals(2, models.size)
            assertEquals("GPT-4", models[0].name)
            assertEquals("Llama-3", models[1].name)
        }
    }
}
