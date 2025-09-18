package com.example.assignment.presentation.state

sealed interface UiEvent {
    data class ShowToast(val message: String) : UiEvent
}


