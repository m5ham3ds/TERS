package com.aistudio.ultimate.domain.usecase

import com.aistudio.ultimate.domain.AiProvider

class RouteRequestUseCase(
    private val providers: Map<String, AiProvider>
) {
    suspend operator fun invoke(providerId: String, prompt: String): String {
        val provider = providers[providerId] ?: throw IllegalArgumentException("Provider not found")
        return provider.sendRequest(prompt)
    }
}
