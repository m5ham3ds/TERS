package com.aistudio.ultimate.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aistudio.ultimate.domain.LocalModelPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "local_models")

class LocalModelPreferencesRepositoryImpl(private val context: Context) : LocalModelPreferencesRepository {
    private val RUNNING_MODELS_KEY = stringSetPreferencesKey("running_models")

    override fun getRunningModels(): Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[RUNNING_MODELS_KEY] ?: emptySet()
        }

    override suspend fun saveRunningModels(models: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[RUNNING_MODELS_KEY] = models
        }
    }
}
