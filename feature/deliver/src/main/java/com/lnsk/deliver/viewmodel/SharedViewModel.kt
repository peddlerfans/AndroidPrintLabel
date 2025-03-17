package com.lnsk.deliver.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnsk.deliver.api.bean.ProductContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class SharedViewModel : ViewModel() {


    private val _deliverDataState =
        MutableStateFlow<List<ProductContent>>(arrayListOf())

    val deliverDataState: StateFlow<List<ProductContent>> get() = _deliverDataState.asStateFlow()

    var orderNo by mutableStateOf<String?>(null)
        private set

    var seekingDevice by mutableStateOf<ProductContent?>(null)

    fun upDeliverData(order: String, list: List<ProductContent>) {
        viewModelScope.launch {
            orderNo = order
            _deliverDataState.emit(list)
            Log.d("_deliverDataState", "User set: ${_deliverDataState.value}") // 添加日志
        }
    }
}

sealed class UiDataState<out T> {
    data object Default : UiDataState<Nothing>()
    data class Data<out T>(val data: T) : UiDataState<T>()
}