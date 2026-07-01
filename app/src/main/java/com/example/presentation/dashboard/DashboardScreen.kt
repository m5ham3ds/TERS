package com.example.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.presentation.AppViewModelProvider

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateToWorkspace: () -> Unit,
    onNavigateToModels: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "AI Studio Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateToWorkspace, modifier = Modifier.fillMaxWidth()) {
            Text("Open Workspace")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToModels, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Models")
        }
    }
}
