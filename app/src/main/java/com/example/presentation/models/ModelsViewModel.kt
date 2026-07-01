package com.example.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AiModel
import com.example.domain.model.ModelFormat
import com.example.domain.repository.ModelRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ModelsViewModel(
    private val modelRepository: ModelRepository
) : ViewModel() {

    // Predefined available local models
    private val _availableModels = MutableStateFlow(
        listOf(
            AiModel("local_llama_3_8b", "Llama 3 (8B)", "local", "3.0", "8B", ModelFormat.GGUF, true),
            AiModel("local_gemma_2b", "Gemma (2B)", "local", "1.0", "2B", ModelFormat.ONNX, true),
            AiModel("local_mistral_7b", "Mistral (7B)", "local", "0.2", "7B", ModelFormat.GGUF, true),
            AiModel("local_phi_3_mini", "Phi-3 Mini", "local", "3.0", "3.8B", ModelFormat.ONNX, true)
        )
    )

    private val _downloadedModels = modelRepository.getModels()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<LocalModelUiState> = combine(
        _availableModels,
        _downloadedModels
    ) { available, downloaded ->
        val downloadedIds = downloaded.map { it.id }.toSet()
        val models = available.map { model ->
            LocalModelItem(
                model = model,
                isDownloaded = downloadedIds.contains(model.id)
            )
        }
        LocalModelUiState(models = models)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LocalModelUiState()
    )

    fun toggleModelDownload(model: AiModel, isDownloaded: Boolean) {
        viewModelScope.launch {
            if (isDownloaded) {
                modelRepository.deleteModel(model)
            } else {
                modelRepository.downloadModel(model)
            }
        }
    }
}

data class LocalModelItem(
    val model: AiModel,
    val isDownloaded: Boolean
)

data class LocalModelUiState(
    val models: List<LocalModelItem> = emptyList()
)
