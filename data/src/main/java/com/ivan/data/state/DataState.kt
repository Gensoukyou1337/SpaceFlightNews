package com.ivan.data.state

sealed class DataState {
    object Loading : DataState()
    data class Success<T>(val data: T) : DataState()
    data class Error(val error: Exception) : DataState()
}