package com.lnsk.deliver.viewmodel

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hardware.ble.BluetoothUtil
import com.lnsk.deliver.api.bean.DeliverBean
import com.lnsk.deliver.api.bean.OrderBean
import com.lnsk.deliver.api.model.Deliver
import com.lnsk.deliver.api.model.OrderState
import com.lnsk.deliver.repository.ApiRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DeliverViewModel : SharedViewModel() {

    var orderUiState by mutableStateOf<UiState<List<OrderBean>>>(UiState.Loading)
        private set

    var deliverUiState by mutableStateOf<UiState<DeliverBean>>(UiState.Loading)
        private set

    var seekDevice by mutableStateOf<UiState<List<ScanResult>>>(UiState.Loading)
        private set

    var showGenerateLabel by mutableStateOf(false)
        private set

    fun init() {
        seekDevice = UiState.Loading
        deliverUiState = UiState.Loading
        showGenerateLabel = false
        bluetoothLeScanner()
    }

    fun setOrderState(orderNo: OrderState){
        viewModelScope.launch {
            ApiRepository.setState(orderNo).onSuccess {

            }
        }

    }

    fun bluetoothLeScanner() {
        BluetoothUtil.getBluetoothLeScanner(
            BluetoothUtil.Builder()
                .scanCallback(object : ScanCallback() {
                    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                        super.onBatchScanResults(results)
                    }

                    override fun onScanFailed(errorCode: Int) {
                        super.onScanFailed(errorCode)
                    }

                    override fun onScanResult(callbackType: Int, result: ScanResult?) {
                        super.onScanResult(callbackType, result)

                        if (result != null) {
                            addSeekDevice(result)
                        }
                    }
                })
                .scanSettings()
                .addFilter(0x5848)
                .addFilter(0x0838)
        )
    }

    fun getOrderList() {
        viewModelScope.launch(IO) {
            orderUiState = UiState.Loading
            ApiRepository.orderList()
                .onSuccess {
                    orderUiState = UiState.Success(it.data)
                }
                .onFailure {
                    orderUiState = UiState.Error(it)
                }
        }
    }


    private val setAddress = hashSetOf<String>()

    fun addSeekDevice(scanResult: ScanResult) {
        when (val state = seekDevice) {
            UiState.Loading -> {
                setAddress.clear()
                if (setAddress.add(scanResult.device.address)) {
                    seekDevice = UiState.Success(arrayListOf(scanResult))
                }
            }

            is UiState.Success -> {
                if (setAddress.add(scanResult.device.address)) {
                    val list = state.data.toMutableList()
                    list.add(scanResult)
                    seekDevice = state.copy(data = list)
                } else {
                    val list = state.data.toMutableList()
                    list.find { it.device.address == scanResult.device.address }?.let {
                        list.remove(it)
                    }
                    list.add(scanResult)
                }
            }

            is UiState.Error -> {}
        }

    }


    fun generateLabel(device: BluetoothDevice,num: Int) {
        showGenerateLabel = true
        seekingDevice?.let { seekingDevice ->
            orderNo?.let { orderNo ->
                var type = 0
                if (device.name.contains("S9") || device.name.contains("YSZJ")) {
                    type = 1
                }
                if (device.name.contains("R12") || device.name.contains("R15")) {
                    type = 2
                }
                if (type == 0) return
                var deliver =Deliver(orderNo, device.address, device.name, seekingDevice.id, type)
                if(num == 0){
                    deliver= Deliver(device.address, device.name, seekingDevice.id.toString(), type)
                }
                getLabelInfo(deliver)
            }
        }
    }


    private fun getLabelInfo(deliver: Deliver) {
        viewModelScope.launch {
            deliverUiState = UiState.Loading
            ApiRepository.getLabelInfo(deliver)
                .onSuccess {
                    if (it.isSuccess()) {
                        deliverUiState = UiState.Success(it.data)
                    } else {
                        deliverUiState = UiState.Error(Error(it.msg))
                    }
                }
                .onFailure {
                    deliverUiState = UiState.Error(it)
                }

        }
    }

    fun resetDeliverUiState() {
        deliverUiState = UiState.Loading
    }

    fun setDeliver (device:DeliverBean){
        deliverUiState = UiState.Loading
        showGenerateLabel = true
        deliverUiState = UiState.Success(device)
        viewModelScope.launch {
            val deliver = Deliver("", device.address, device.name, -1, device.type)
            ApiRepository.getLabelInfo(deliver)
        }
    }
}


sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val error: Throwable) : UiState<Nothing>()
}
