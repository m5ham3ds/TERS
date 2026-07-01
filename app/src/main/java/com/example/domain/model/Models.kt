package com.example.domain.model

enum class ModelFormat {
    GGUF, ONNX, TFLITE, CLOUD
}

data class AiModel(
    val id: String,
    val name: String,
    val providerId: String,
    val version: String,
    val capacity: String, // e.g., "7B", "14B"
    val format: ModelFormat,
    val isLocal: Boolean
)

data class Provider(
    val id: String,
    val name: String,
    val authType: AuthType
)

enum class AuthType {
    API_KEY, OAUTH2, BASIC_AUTH, NONE
}

data class Agent(
    val id: String,
    val name: String,
    val role: String,
    val modelId: String,
    val tools: List<String>
)
