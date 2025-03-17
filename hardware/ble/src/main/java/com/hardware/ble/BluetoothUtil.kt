package com.hardware.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import androidx.annotation.RequiresPermission

class BluetoothUtil private constructor() {

    lateinit var context: Context

    companion object {

        private val mBluetoothUtil: BluetoothUtil by lazy { BluetoothUtil() }


        val bluetoothManager: BluetoothManager by lazy {
            mBluetoothUtil.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        }

        val bluetoothAdapter: BluetoothAdapter by lazy { bluetoothManager.adapter }

        fun init(context: Context) {
            mBluetoothUtil.context = context
        }

        @RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN)
        fun getBluetoothLeScanner(builder: Builder) {
            bluetoothAdapter.bluetoothLeScanner.startScan(
                builder.scanFilterList,
                builder.scanSettings,
                builder.scanCallback
            )
        }
    }


    class Builder {

        //扫描过滤器列表
        var scanFilterList: ArrayList<ScanFilter> = ArrayList()
            private set

        //5.0扫描配置对象
        lateinit var scanSettings: ScanSettings
            private set

        //5.0及其以上扫描回调对象
        lateinit var scanCallback: ScanCallback
            private set

        fun addFilter(manufacturer: Int): Builder {
            addFilterManufacturer(manufacturer)
            return this
        }

        fun addFilterManufacturer(
            manufacturerId: Int,
            manufacturerData: ByteArray = byteArrayOf()
        ): Builder {
            val builder = ScanFilter.Builder()
            builder.setManufacturerData(manufacturerId, manufacturerData)
            scanFilterList.add(builder.build())
            return this
        }

        fun scanCallback(
            callback: ScanCallback = object : ScanCallback() {
                override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                    super.onBatchScanResults(results)
                }

                override fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)

                }

                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)
                }
            }
        ): Builder {
            scanCallback = callback
            return this
        }

        fun scanSettings(
            settings: ScanSettings.Builder.() -> ScanSettings = {
                setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                //设置功耗平衡模式
                // setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                //设置高功耗模式
                setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)

                //android 6.0添加设置回调类型、匹配模式等
                //定义回调类型
                setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                //设置蓝牙LE扫描滤波器硬件匹配的匹配模式
                setMatchMode(ScanSettings.MATCH_MODE_STICKY)
                //芯片组支持批处理芯片上的扫描
                if (bluetoothAdapter.isOffloadedScanBatchingSupported()) {
                    //设置蓝牙LE扫描的报告延迟的时间（以毫秒为单位）
                    //设置为0以立即通知结果
                    setReportDelay(0L)
                }
                this.build()
            }
        ): Builder {
            scanSettings = ScanSettings.Builder().settings()
            return this
        }

    }

}