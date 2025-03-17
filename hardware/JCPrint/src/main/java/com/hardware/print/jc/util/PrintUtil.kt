package com.hardware.print.jc.util

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.graphics.Bitmap
import android.util.Log
import com.gengcon.www.jcprintersdk.JCPrintApi
import com.gengcon.www.jcprintersdk.callback.Callback
import com.gengcon.www.jcprintersdk.callback.PrintCallback
import java.io.File


/**
 * 打印工具类
 *
 * @author zhangbin
 */
object PrintUtil {
    private const val TAG = "PrintUtil"
    var connectedType: Int = -1

    val ERROR_MESSAGES: MutableMap<Int, String> = java.util.HashMap<Int, String>().apply {
        this[1] = "盒盖打开";
        this[2] = "缺纸";
        this[3] = "电量不足";
        this[4] = "电池异常";
        this[5] = "手动停止";
        this[6] = "数据错误";
        this[7] = "温度过高";
        this[8] = "出纸异常";
        this[9] = "正在打印";
        this[10] = "没有检测到打印头";
        this[11] = "环境温度过低";
        this[12] = "打印头未锁紧";
        this[13] = "未检测到碳带";
        this[14] = "不匹配的碳带";
        this[15] = "用完的碳带";
        this[16] = "不支持的纸张类型";
        this[17] = "纸张类型设置失败";
        this[18] = "打印模式设置失败";
        this[19] = "设置浓度失败";
        this[20] = "写入rfid失败";
        this[21] = "边距设置失败";
        this[22] = "通讯异常";
        this[23] = "打印机连接断开";
        this[24] = "画板参数错误";
        this[25] = "旋转角度错误";
        this[26] = "json参数错误";
        this[27] = "出纸异常(B3S)";
        this[28] = "检查纸张类型";
        this[29] = "RFID标签未进行写入操作";
        this[30] = "不支持浓度设置";
        this[31] = "不支持的打印模式";
    }


    /**
     * 回调接口，用于处理打印机状态变化事件
     */
    private val CALLBACK: Callback = object : Callback {
        private val TAG = "PrintUtil"

        /**
         * 连接成功回调
         *
         * @param address 设备地址，蓝牙为蓝牙 MAC 地址，WIFI 为 IP 地址
         * @param type   连接类型，0 表示蓝牙连接，1 表示 WIFI 连接
         */
        override fun onConnectSuccess(address: String, type: Int) {
            connectedType = type
            printConnectionCallback(type)
        }

        /**
         * 断开连接回调
         * 当设备断开连接时，将调用此方法。
         */
        override fun onDisConnect() {
            connectedType = -1
            printConnectionCallback(-1)
        }

        /**
         * 电量变化回调
         * 当设备电量发生变化时，将调用此方法。
         *
         * @param powerLevel 电量等级，取值范围为 1 到 4，代表有 1 到 4 格电，满电是 4 格
         */
        override fun onElectricityChange(powerLevel: Int) {
        }

        /**
         * 监测上盖状态变化回调
         * 当上盖状态发生变化时，将调用此方法。目前该回调仅支持 H10/D101/D110/D11/B21/B16/B32/Z401/B3S/B203/B1/B18 系列打印机。
         *
         * @param coverStatus 上盖状态，0 表示上盖打开，1 表示上盖关闭
         */
        override fun onCoverStatus(coverStatus: Int) {
        }

        /**
         * 监测纸张状态变化
         * 当纸张状态发生变化时，将调用此方法。目前该回调仅支持H10/D101/D110/D11/B21/B16/B32/Z401/B203/B1/B18 系列打印机。
         *
         * @param paperStatus 0为不缺纸 1为缺纸
         */
        override fun onPaperStatus(paperStatus: Int) {
        }

        /**
         * 监测标签rfid读取状态变化
         * 当标签rfid读取状态发生变化时，将调用此方法。
         *
         * @param rfidReadStatus 0为未读取到标签RFID 1为成功读取到标签RFID 目前该回调仅支持H10/D101/D110/D11/B21/B16/B32/Z401/B203/B1/B18 系列打印机。
         */
        override fun onRfidReadStatus(rfidReadStatus: Int) {
        }


        /**
         * 监测碳带rfid读取状态变化
         * 当碳带rfid读取状态发生变化时，将调用此方法。
         *
         * @param ribbonRfidReadStatus 0为未读取到碳带RFID 1为成功读取到碳带RFID 目前该回调仅支持B18/B32/Z401/P1/P1S 系列打印机。
         */
        override fun onRibbonRfidReadStatus(ribbonRfidReadStatus: Int) {
        }

        /**
         * 监测碳带状态变化
         * 当纸张状态发生变化时，将调用此方法
         *
         * @param ribbonStatus 0为无碳带 1为有碳带 目前该回调仅支持B18/B32/Z401/P1/P1S系列打印机。
         */
        override fun onRibbonStatus(ribbonStatus: Int) {
        }


        /**
         * 固件异常回调，需要升级
         * 当设备连接成功但出现固件异常时，将调用此方法，表示需要进行固件升级。
         */
        override fun onFirmErrors() {
        }
    }


    /**
     * 单例实例，使用 volatile 保证多线程可见性和有序性
     */
    private val instance: JCPrintApi by lazy { JCPrintApi.getInstance(CALLBACK) }

    fun init(application: Application) {
        instance.apply {
            instance.initSdk(application)
            //获取内置目录路径
            val directory = application.filesDir
            //获取自定义字体路径
            val customFontDirectory = File(directory, "custom_font")
            instance.initDefaultImageLibrarySettings(customFontDirectory.absolutePath, "")
        }

    }

    private var printConnectionCallback: (connectedType: Int) -> Unit = {}
    private var printCallback: (printType: Int) -> Unit = {}

    @JvmOverloads
    fun setOnPrintConnectionCallback(printConnectionCallback: (connectedType: Int) -> Unit = {}) {

        this.printConnectionCallback = printConnectionCallback

    }

    fun setOnPrintCallback(printCallback: (printType: Int) -> Unit = {}) {
        this.printCallback = printCallback
    }

    fun isConnection(): Boolean {
        return instance.isConnection() == 0
    }

    /**
     * 通过打印机mac地址进行蓝牙连接开启打印机（同步）
     *
     * @param address 打印机地址
     * @return 成功与否
     */
    fun connectBluetoothPrinter(address: String) = instance.connectBluetoothPrinter(address)

    /**
     * 与设备配对
     *
     * @param btDevice 要配对的蓝牙设备
     * @return 配对是否成功
     * @throws Exception 反射调用可能引发异常
     */
    @Throws(Exception::class)
    fun createBond(btDevice: BluetoothDevice): Boolean {
        // 获取 BluetoothDevice 类的类对象
        val btClass: Class<*> = BluetoothDevice::class.java

        // 获取 createBond 方法的引用
        val createBondMethod = btClass.getMethod("createBond")
        // 调用 createBond 方法，返回配对结果
        return createBondMethod.invoke(btDevice) as Boolean
    }

    /**
     * 关闭打印机
     */
    fun close() = instance.close()

    /**
     * 检查打印机是否连接
     *
     * @return 连接状态代码
     */
    val isConnection: Int = instance.isConnection()

    /**
     * 是否打印错误
     */
    private var isError = false

    /**
     * 是否取消打印
     */
    private var isCancel = false

    /**
     * 是否打印
     * */
    @Volatile
    private var isPrint = false

    /**
     * 提交图像打印数据
     * 该⽅法⽤于提交打印数据并触发打印任务，包括打印⽅向、打印位图、⻚⾯宽度、⻚⾯⾼度、打 印数量、边距和 RFID 等参数。
     *
     * @param orientation         打印⽅向，取值为 0、90、 180 或 270，分别表示 0 度、90度、180 度或 270 度的旋转
     * @param printBitmap         打印位图，要打印的图像的位图表示
     * @param pageWidth           ⻚⾯宽度，打印⻚⾯的宽度，单位为 mm
     * @param pageHeight          ⻚⾯⾼度，打印⻚⾯的⾼度，单位为 mm
     * @param printQuantity       打印数量，要打印的副本数量
     * @param marginTop           ⻚⾯上边距，打印内容距离⻚⾯顶部的距离，单位为mm
     * @param marginLeft          ⻚⾯左边距，打印内容距离⻚⾯左侧的距离，单位为 mm
     * @param marginBottom        ⻚⾯下边距，打印内容距离⻚⾯底部的距离，单位为 mm
     * @param marginRight         ⻚⾯右边距，打印内容距离⻚⾯右侧的距离，单位为 mm
     * @param rfid                RFID 数据，要写⼊打印任务的RFID数据，
     *                            EPC字符的⻓度应为4的倍数且仅允许包含数字和⼩写字⺟，
     *                            形式为 "0123456789abcdef"，每 4 个字符表示⼀个 16 位的字（仅 B32R 打印 RFID标签是传⼊数据，
     *                            其他场景默认"" ）
     */
    fun print(
        printBitmap: Bitmap,
        orientation: Int = 0,
        pageWidth: Float = 0f,
        pageHeight: Float = 0f,
        printQuantity: Int = 0,
        marginTop: Int = 0,
        marginLeft: Int = 0,
        marginBottom: Int = 0,
        marginRight: Int = 0,
        rfid: String = "",
        onError: (errorCode: Int, printState: Int) -> Unit = { _, _ -> }
    ) {

        // 重置错误和取消打印状态
        isError = false
        isCancel = false
        isPrint = false
        // 传⼊浓度，纸张类型，打印模式，打印状态回调
        val printDensity = 3
        //  纸张类型，可选值：1:间隙纸; 2:⿊标纸; 3:连续纸; 4:定孔纸; 5:透明纸; 6:标牌;
        val paperType = 1
        // 打印模式，可选值：1热敏，2热转印
        val printModel = 1
        instance.setTotalPrintQuantity(1)
        instance.startPrintJob(printDensity, paperType, printModel, object : PrintCallback {
            /**
             * 打印进度回调
             * 该⽅法⽤于在打印过程中回调打印进度信息，包括已打印的⻚数、当前⻚已打印的份数以及⾃定义 数据（在⽀持 RFID 的机型中）。
             *
             * @param pageIndex 已打印的⻚数
             * @param quantityIndex 当前⻚已打印的份数
             * @param customData ⾃定义数据，⽀持 RFID 的机型回调数据，默认不处理即可
             */
            override fun onProgress(
                pageIndex: Int,
                quantityIndex: Int,
                customData: HashMap<String, Any>?
            ) {
//                PrintUtil.getInstance().endPrintJob()
                Log.e(TAG, "打印进度回调 $pageIndex $quantityIndex $customData")
                instance.endPrintJob()
            }

            override fun onError(i: Int) {
                Log.e(TAG, "onError $i")
            }


            /**
             * 打印异常回调
             * 该⽅法⽤于在打印过程中回调打印异常信息，包括错误码和当前打印状态
             *
             * @param errorCode 错误码，具体错误码含义如下*    1—盒盖打开；2—缺纸；3—电量不⾜；4—电池异常；5—⼿动停⽌（按键）；*
             *                                           6—数据错误；7—温度过⾼;8出纸异常，*
             *                                           9正在打印10.没有检测到打印头11.环境温度过低12.打印头未锁紧*
             *                                           13未检测到碳带14不匹配的碳带15⽤完的碳带16不⽀持的纸张类型，17纸张类型设置失败，18打印模式设置失败，*
             *                                           19设置浓度失败，20写⼊rfid失败，21-边距设置失败，22通讯异常，23打印机连接断开，24画板参数错误，*
             *                                           25旋转⻆度错误，26-json参数错误，27出纸异常（B3S），28检查纸张类型29-RFID标签未进⾏写⼊操作，*
             *                                           30不⽀持浓度设置；31不⽀持的打印模式，32标签材质设置失败，33不⽀持的标签材质设置
             *
             *  @param printState  当前打印状态， 0 表示在打印， 1 表示打印暂停， 2 表示打印停⽌
             * */
            override fun onError(errorCode: Int, printState: Int) {
                Log.e(TAG, "打印异常回调 $errorCode $printState")
                onError(errorCode, printState)
            }

            /**
             * SDK 缓存队列空闲回调
             * 该⽅法⽤于在 SDK 缓存队列空闲时触发回调，此时可以提交打印数据。
             *
             * @param pageIndex 下⼀⻚的打印索引
             * @param bufferSize 缓冲区⼤⼩
             */
            override fun onBufferFree(pageIndex: Int, bufferSize: Int) {
                Log.e(TAG, "SDK 缓存队列空闲回调 $pageIndex $bufferSize")

                /*
                 * 1.如果未结束打印，且SDK缓存出现空闲，则自动回调该接口，此回调会上报多次，直到打印结束。
                 * 2.打印过程中，如果出现错误、取消打印，或 pageIndex 超过总页数，则返回。(此处控制代码必须得保留，否则会导致打印失败)
                 */
                if (isError || isCancel /*|| pageIndex > pageCount*/) {
                    return
                }

                if (!isPrint) {
                    isPrint = true
                    Log.e(TAG, "commitImageData:$isPrint")
                    instance.commitImageData(
                        orientation,
                        printBitmap,
                        pageWidth,
                        pageHeight,
                        printQuantity,
                        marginTop,
                        marginLeft,
                        marginBottom,
                        marginRight,
                        rfid
                    )
                }
            }

            /**
             * 取消打印任务回调
             * 该⽅法⽤于在取消打印任务时回调取消的结果。
             *
             * @param isSuccess 取消结果， true 表示取消成功， false 表示取消失败
             */
            override fun onCancelJob(isSuccess: Boolean) {
                Log.e(TAG, "取消打印任务回调 $isSuccess")
                isCancel = true
            }
        })
    }
}
