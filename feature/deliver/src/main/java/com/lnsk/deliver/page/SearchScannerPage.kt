package com.lnsk.deliver.page

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hardware.ble.BluetoothUtil
import com.hardware.print.jc.util.PrintUtil

@Preview
@Composable
fun SearchScannerPage() {

    val list = remember { mutableStateListOf<ScanResult>() }


    val setAddress = remember { mutableSetOf<String>() }

    LaunchedEffect(Unit) {
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
                            if (setAddress.add(result.device.address) && result.device.name?.isNotEmpty() == true) {
                                list.add(result)
                            }
                        }
                    }
                })
                .scanSettings()
//                .addFilter(0x5848)
//                .addFilter(0x0838)
        )
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(list) {
            Row(Modifier.fillMaxWidth().background(Color.White)) {
                Column {
                    Text(it.device.name)
                    Text(it.device.address)
                }

                Button({
//                    PrintUtil.connectBluetoothPrinter(it.device.address)
                    try {
                        if (PrintUtil.createBond(it.device)) {
                            PrintUtil.connectBluetoothPrinter(it.device.address)
                        }
                    } catch (e: Exception) {
                        Log.d("TAG", "闪退日志" + e.message)
                    }
                }) {
                    Text("连接")
                }
            }
        }
    }
}