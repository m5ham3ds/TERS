package com.example.presentation.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Workspace : Screen("workspace")
    object ModelManager : Screen("model_manager")
    object AgentStudio : Screen("agent_studio")
    object Settings : Screen("settings")
}
