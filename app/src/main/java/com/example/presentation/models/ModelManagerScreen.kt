package com.example.presentation.models

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.presentation.AppViewModelProvider

@Composable
fun ModelManagerScreen(
    viewModel: ModelsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val models by viewModel.models.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Installed Models", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        if (models.isEmpty()) {
            Text("No models installed yet.")
        } else {
            LazyColumn {
                items(models) { model ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = model.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Provider: ${model.providerId}")
                            Text(text = "Format: ${model.format}")
                        }
                    }
                }
            }
        }
    }
}
