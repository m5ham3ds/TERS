package com.example.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AiModel
import com.example.domain.usecase.GetModelsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ModelsViewModel(
    getModelsUseCase: GetModelsUseCase
) : ViewModel() {

    val models: StateFlow<List<AiModel>> = getModelsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
