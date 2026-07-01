package com.example.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ProviderApi {
    @POST
    suspend fun generateCompletion(
        @Url url: String,
        @Header("Authorization") authHeader: String,
        @Body request: Map<String, @JvmSuppressWildcards Any>
    ): Map<String, @JvmSuppressWildcards Any>
}
