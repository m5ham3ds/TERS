package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.remote.ProviderApi
import com.example.data.remote.RetrofitClient
import com.example.data.repository.ModelRepositoryImpl
import com.example.domain.repository.ModelRepository
import com.example.domain.usecase.GetModelsUseCase

class AppContainer(private val context: Context) {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ai_studio_db"
        ).build()
    }

    val providerApi: ProviderApi by lazy {
        RetrofitClient.providerApi
    }

    val modelRepository: ModelRepository by lazy {
        ModelRepositoryImpl(database.modelDao())
    }

    val getModelsUseCase: GetModelsUseCase by lazy {
        GetModelsUseCase(modelRepository)
    }
}
