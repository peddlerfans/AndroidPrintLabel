package com.lnsk.labelprinting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MainPage(navController)
//            TestList(PaddingValues(10.dp))
        }
    }
}

//
//@Composable
//private fun TestList(innerPadding: PaddingValues) {
//    Column(
//        Modifier
//            .padding(innerPadding)
//            .fillMaxSize()
//            .background(Color.Red),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
////                    PrintBitmap()
//        CanvasToBitmapExample()
//    }
//}
//
//@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
//@Preview
//@Composable
//fun CanvasToBitmapExample() {
//
//    val captureController = rememberCaptureController()
//    val scope = rememberCoroutineScope()
//    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
//
//    Column {
//        Column(modifier = Modifier.capturable(captureController)) {
//            PrintBitmap()
//        }
//
//        Button(onClick = {
//            // 捕捉内容
//            scope.launch {
//                val bitmapAsync = captureController.captureAsync(config = Bitmap.Config.RGB_565)
//                try {
//                    bitmapAsync.await().also {
//                        val options = BitmapFactory.Options().apply {
//                            inPreferredConfig = Bitmap.Config.RGB_565  // 使用 RGB_565 格式
//                            inJustDecodeBounds = false
//                        }
//                        bitmap = it.asAndroidBitmap()
//                    }
//                    // 对 `bitmap` 进行操作。
//                } catch (error: Throwable) {
//                    // 发生错误，进行处理。
//                }
//            }
//        }) {
//            Text("生成图片")
//        }
//
//        Button({
//            PrintUtil.connectBluetoothPrinter("12:28:0B:E9:C4:87")
//        }) {
//            Text("连接:12:28:0B:E9:C4:87")
//        }
//
//        Button({
//            scope.launch(IO) {
//                bitmap?.let {
//                    val bitmapWidth = bitmap!!.width
//                    val bitmapHeight = bitmap!!.height
//
//                    val width = bitmap!!.width
//                    val height = bitmap!!.height
//                    val byteCount: Int
//
//
//                    when (bitmap!!.config) {
//                        Bitmap.Config.ARGB_8888 -> byteCount = width * height * 4
//                        Bitmap.Config.RGB_565 -> byteCount = width * height * 2
//                        Bitmap.Config.ALPHA_8 -> byteCount = width * height * 1
////                        Bitmap.Config.RGBA_F16 -> byteCount = width * height * 8
//                        else -> byteCount = 0
//                    }
//
//                    println("Bitmap size: ${byteCount / 1024f} KB")
//
//                    PrintUtil.print(
//                        0, it,
//                        40f,
//                        30f,
//                        1,
//                        0,
//                        0,
//                        0,
//                        0,
//                        ""
//                    )
//                }
//            }
//
//        }) {
//            Text("打印")
//        }
//
//        Button({
//            scope.launch(IO) {
//                //创建小票图片
//                val bmp = Bitmap.createBitmap(
//                    400,
//                    300,
//                    Bitmap.Config.ARGB_8888
//                )
//                val canvas = Canvas(bmp)
//                val paint = Paint().apply {
//                    textSize = 48f
//                    color = android.graphics.Color.BLACK
//                    style = Paint.Style.FILL
//                    typeface = Typeface.DEFAULT_BOLD
//                    isAntiAlias = false
//                    isDither = false
//                    hinting = Paint.ANTI_ALIAS_FLAG
//                }
//                canvas.drawColor(android.graphics.Color.WHITE)
//
//                val deliverBean: DeliverBean = DeliverBean(address = "dsafs", name = "sdafasd")
//                canvas.drawText(
//                    deliverBean.name, 0f,
//                    ImgUtil.getFontHeight(paint, deliverBean.name).toFloat(), paint
//                )
//
////                canvas.drawText("测试2", 10f, 10f, paint)
////                canvas.drawText("测试2", 50f, 50f, paint)
//                PrintUtil.print(
//                    0, bmp,
//                    40f,
//                    30f,
//                    1,
//                    0,
//                    0,
//                    0,
//                    0,
//                    ""
//                )
//            }
//
//        }) {
//            Text("打印2")
//        }
//
//        bitmap?.let {
//            Image(bitmap = it.asImageBitmap(), "测试")
//        }
//    }
//}

