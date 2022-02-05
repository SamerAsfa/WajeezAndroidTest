package com.blackapp.wajeezandroiddevelopertask.common.data_state

sealed class DataState <out T> {
    data class Success<out T>(val data: T): DataState<T>()
    data class Failed(val exception: Exception) : DataState<Nothing>()
    data class GeneralError(val exception: String) : DataState<Nothing>()
    object Loading: DataState<Nothing>()
    object Non: DataState<Nothing>()

}

