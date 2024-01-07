package com.squareup.android.directory.model

import javax.annotation.concurrent.Immutable

@Immutable
sealed interface UiState<out R> {
    data object Loading : UiState<Nothing>
    data class Error(val errorMessage: String? = null) : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
}
