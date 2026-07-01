package com.aistudio.ultimate.domain

import kotlinx.coroutines.flow.Flow

/**
 * Represents an AI Model configuration.
 */
interface AiModel {
    val id: String
    val name: String
    val provider: String
    val version: String
    val capacity: Int
    val method: String
}

/**
 * Represents an AI Provider that can send requests and validate connections.
 */
interface AiProvider {
    val id: String
    val name: String
    
    /**
     * Sends a request to the provider.
     * @param prompt The input text.
     * @return The response text.
     */
    suspend fun sendRequest(prompt: String): String
    
    /**
     * Validates if the connection to the provider is successful.
     * @return true if connection is valid, false otherwise.
     */
    suspend fun validateConnection(): Boolean
}

/**
 * Represents an Agent in the Multi-Agent System.
 */
interface Agent {
    val id: String
    val name: String
    val role: String
    val model: AiModel
    val tools: List<String>
    
    /**
     * Executes a task using the agent.
     */
    suspend fun executeTask(task: String): String
}

/**
 * Manages the memory and context for agents.
 */
interface MemoryManager {
    suspend fun saveContext(context: String)
    suspend fun getRelevantContext(query: String): String
}

data class ModelMetrics(
    val memoryUsageMb: Int,
    val tokensPerSecond: Float
)

/**
 * Manages the runtime lifecycle of local AI models.
 */
interface LocalModelEngine {
    suspend fun startModel(modelId: String)
    suspend fun stopModel(modelId: String)
    suspend fun isModelRunning(modelId: String): Boolean
    fun getModelMetrics(modelId: String): Flow<ModelMetrics>
}

interface LocalModelPreferencesRepository {
    fun getRunningModels(): Flow<Set<String>>
    suspend fun saveRunningModels(models: Set<String>)
}
