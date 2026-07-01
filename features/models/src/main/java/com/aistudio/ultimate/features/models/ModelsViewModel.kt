package com.aistudio.ultimate.features.models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.aistudio.ultimate.domain.LocalModelEngine
import com.aistudio.ultimate.domain.LocalModelPreferencesRepository
import com.aistudio.ultimate.domain.ModelMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class ModelsViewModel(
    private val localModelEngine: LocalModelEngine,
    private val localModelPreferencesRepository: LocalModelPreferencesRepository? = null,
    private val workManager: WorkManager? = null
) : ViewModel() {

    private val _availableModels = MutableStateFlow(listOf(
        AiModelInfo("llama_3_8b", "Llama 3 (8B)", "Meta", "3.0", "8B", "GGUF", true, "Llama 3 License", "8B", "Q4_K_M"),
        AiModelInfo("gemma_2b", "Gemma (2B)", "Google", "1.0", "2B", "ONNX", true, "Apache 2.0", "2B", "FP16"),
        AiModelInfo("mistral_7b", "Mistral (7B)", "Mistral AI", "0.2", "7B", "GGUF", true, "Apache 2.0", "7B", "Q5_K_M"),
        AiModelInfo("phi_3_mini", "Phi-3 Mini", "Microsoft", "3.0", "3.8B", "ONNX", true, "MIT", "3.8B", "INT4")
    ))

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFormatFilter = MutableStateFlow<String?>(null)
    val selectedFormatFilter: StateFlow<String?> = _selectedFormatFilter.asStateFlow()

    val filteredModels: Flow<List<AiModelInfo>> = combine(
        _availableModels,
        _searchQuery,
        _selectedFormatFilter
    ) { models, query, format ->
        models.filter { model ->
            val matchesQuery = model.name.contains(query, ignoreCase = true) ||
                               model.provider.contains(query, ignoreCase = true)
            val matchesFormat = format == null || model.format.equals(format, ignoreCase = true)
            matchesQuery && matchesFormat
        }
    }

    private val _runningModels = MutableStateFlow<Set<String>>(emptySet())
    val runningModels: StateFlow<Set<String>> = _runningModels.asStateFlow()

    private val _isStorageLow = MutableStateFlow(true) // Simulating low storage state
    val isStorageLow: StateFlow<Boolean> = _isStorageLow.asStateFlow()

    private val _downloadingModels = MutableStateFlow<Map<String, Int>>(emptyMap())
    val downloadingModels: StateFlow<Map<String, Int>> = _downloadingModels.asStateFlow()

    init {
        viewModelScope.launch {
            localModelPreferencesRepository?.getRunningModels()?.collect { savedRunning ->
                val current = _runningModels.value.toMutableSet()
                savedRunning.forEach { id ->
                    if (!localModelEngine.isModelRunning(id)) {
                        localModelEngine.startModel(id)
                    }
                    current.add(id)
                }
                _runningModels.value = current
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFormatFilter(format: String?) {
        _selectedFormatFilter.value = format
    }

    fun dismissStorageWarning() {
        _isStorageLow.value = false
    }

    fun toggleModel(modelId: String) {
        viewModelScope.launch {
            val isRunning = localModelEngine.isModelRunning(modelId)
            val current = _runningModels.value.toMutableSet()
            if (isRunning) {
                localModelEngine.stopModel(modelId)
                current.remove(modelId)
            } else {
                localModelEngine.startModel(modelId)
                current.add(modelId)
            }
            _runningModels.value = current
            localModelPreferencesRepository?.saveRunningModels(current)
        }
    }

    fun getMetrics(modelId: String): Flow<ModelMetrics> {
        return localModelEngine.getModelMetrics(modelId)
    }

    fun deleteModel(modelId: String) {
        val currentModels = _availableModels.value.toMutableList()
        currentModels.removeAll { it.id == modelId }
        _availableModels.value = currentModels

        viewModelScope.launch {
            if (localModelEngine.isModelRunning(modelId)) {
                localModelEngine.stopModel(modelId)
                val currentRunning = _runningModels.value.toMutableSet()
                currentRunning.remove(modelId)
                _runningModels.value = currentRunning
                localModelPreferencesRepository?.saveRunningModels(currentRunning)
            }
        }
    }

    fun downloadModel(modelId: String) {
        if (workManager == null) return

        val workRequest = OneTimeWorkRequestBuilder<ModelDownloadWorker>()
            .setInputData(workDataOf(ModelDownloadWorker.KEY_MODEL_ID to modelId))
            .build()

        workManager.enqueue(workRequest)

        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(workRequest.id).collect { workInfo ->
                if (workInfo != null) {
                    val progress = workInfo.progress.getInt(ModelDownloadWorker.KEY_PROGRESS, 0)
                    val currentDownloading = _downloadingModels.value.toMutableMap()
                    
                    if (workInfo.state == WorkInfo.State.SUCCEEDED || workInfo.state == WorkInfo.State.FAILED || workInfo.state == WorkInfo.State.CANCELLED) {
                        currentDownloading.remove(modelId)
                    } else {
                        currentDownloading[modelId] = progress
                    }
                    _downloadingModels.value = currentDownloading
                }
            }
        }
    }
}

data class AiModelInfo(
    val id: String,
    val name: String,
    val provider: String,
    val version: String,
    val capacity: String,
    val format: String,
    val isLocal: Boolean,
    val license: String = "Unknown",
    val parameterCount: String = capacity,
    val quantizationMethod: String = "Unknown"
)
