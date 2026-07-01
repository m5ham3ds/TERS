package com.aistudio.ultimate.domain.usecase

import com.aistudio.ultimate.domain.AiProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RouteRequestUseCaseTest {

    @Test
    fun `invoke should route to correct provider and return response`() = runBlocking {
        val mockProvider = object : AiProvider {
            override val id = "mock"
            override val name = "Mock Provider"
            override suspend fun sendRequest(prompt: String): String {
                return "Response to $prompt"
            }
            override suspend fun validateConnection() = true
        }

        val providers = mapOf("mock" to mockProvider)
        val useCase = RouteRequestUseCase(providers)

        val result = useCase.invoke("mock", "Hello")
        
        assertEquals("Response to Hello", result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `invoke should throw exception when provider not found`() = runBlocking {
        val useCase = RouteRequestUseCase(emptyMap())
        useCase.invoke("unknown", "Hello")
        Unit
    }
}
