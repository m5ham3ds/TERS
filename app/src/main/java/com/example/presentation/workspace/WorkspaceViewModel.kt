package com.example.presentation.workspace

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkspaceViewModel() : ViewModel() {
    private val _chatMessages = MutableStateFlow<List<String>>(emptyList())
    val chatMessages: StateFlow<List<String>> = _chatMessages
    
    fun sendMessage(message: String) {
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(message)
        _chatMessages.value = currentList
    }
}
