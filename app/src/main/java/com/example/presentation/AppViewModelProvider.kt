package com.example.presentation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.AiStudioUltimateApp
import com.example.presentation.dashboard.DashboardViewModel
import com.example.presentation.models.ModelsViewModel
import com.example.presentation.workspace.WorkspaceViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DashboardViewModel()
        }
        initializer {
            WorkspaceViewModel()
        }
        initializer {
            ModelsViewModel(
                aiStudioApplication().container.modelRepository
            )
        }
    }
}

fun CreationExtras.aiStudioApplication(): AiStudioUltimateApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AiStudioUltimateApp)
