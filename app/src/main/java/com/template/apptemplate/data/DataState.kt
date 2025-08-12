package com.template.apptemplate.data

sealed class DataState<out T> {

    /**State of success containing [data]*/
    data class Success<T>(val data: T) : DataState<T>()

    /**State of idle*/
    data object Idle : DataState<Nothing>()

    /** State of loading.*/
    data object Loading : DataState<Nothing>()

    /** State of error containing [Error.message]*/
    data class Error(val message: String) : DataState<Nothing>()

    /** Getting error message.
     * @return
     * - [DataState.Error.message] if the state is error
     * - null if the state is not [DataState.Error].*/
    fun getErrorMessage(): String? {
        return when (this) {
            is Success -> null
            is Idle -> null
            is Loading -> null
            is Error -> message
        }
    }

    /** Getting data of success state.
     * @return
     * - [DataState.Success.data] if the state is success
     * - null if the state is not [DataState.Success] or if the data is success but the data is null.*/
    fun getStateData(): T? {
        return when (this) {
            is Success -> data
            is Idle -> null
            is Loading -> null
            is Error -> null
        }
    }

    fun isIdle() = this is Idle

    fun isError() = this is Error

    fun isLoading() = this is Loading

    fun isSuccess() = this is Success
}