package com.squareup.android.directory.model

import javax.annotation.concurrent.Immutable

@Immutable
sealed interface UiState<out R> {
    data object Loading : UiState<Nothing>
    data class Error(val errorMessage: String? = null) : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
}

val <T> UiState<T>.data: T?
    get() = (this as? UiState.Success)?.data

val <T> UiState<T>.errorMessage: String?
    get() = (this as? UiState.Error)?.errorMessage
