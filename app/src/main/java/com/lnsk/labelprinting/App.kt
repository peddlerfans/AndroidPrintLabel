package com.lnsk.labelprinting

import android.app.Application
import com.hardware.ble.BluetoothUtil
import com.hardware.print.jc.util.PrintUtil

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        PrintUtil.init(this)
        BluetoothUtil.init(this)
    }
}