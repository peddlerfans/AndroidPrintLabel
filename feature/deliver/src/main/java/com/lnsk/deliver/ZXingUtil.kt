@file:JvmName("ZXingUtil")

package com.lnsk.deliver

import android.graphics.*
import android.graphics.Color.BLACK
import android.text.TextUtils
import android.util.Log
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.king.zxing.DecodeFormatManager
import com.king.zxing.util.CodeUtils
import java.util.*

/**
 * data 数据
 * 生成二维码
 */
@JvmOverloads
fun String.createQRCode(heightPix: Int = 600, logo: Bitmap? = null): Bitmap {
    //生成二维码
    return CodeUtils.createQRCode(this, heightPix, logo)
}


@JvmOverloads
fun String.createBarCode(desiredWidth: Int = 220, desiredHeight: Int = 60): Bitmap {
    return CodeUtils.createBarCode(this, desiredWidth, desiredHeight)
}

@JvmOverloads
@Synchronized
fun String.createBarCodeText(brWidth: Int = 220, qrHeight: Int = 60): Bitmap {
    val text = this.trim { it <= ' ' }
    //文字的高度
    val mHeight = qrHeight / 4
    return try {
        val hints: MutableMap<EncodeHintType, Any?> = EnumMap(
            EncodeHintType::class.java
        )
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        hints[EncodeHintType.MARGIN] = 1
        val result: BitMatrix = try {
            MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, brWidth, mHeight * 3, hints)
        } catch (iae: IllegalArgumentException) {
            iae.printStackTrace()
            return Bitmap.createBitmap(brWidth, qrHeight, Bitmap.Config.ARGB_8888)
        }
        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result[x, y]) BLACK else 0
            }
        }
        val qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        qrBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        //大的bitmap
        val bigBitmap = Bitmap.createBitmap(width, qrHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bigBitmap)
        val srcRect = Rect(0, 0, width, height)
        val dstRect = Rect(0, 0, width, height)
        canvas.drawBitmap(qrBitmap, srcRect, dstRect, null)
        val p = Paint()
        p.color = BLACK
        p.isFilterBitmap = true
        //字体大小
        p.textSize = mHeight.toFloat()
        //开始绘制文本的位置
        canvas.translate(width / 2f, mHeight.toFloat())
        p.textAlign = Paint.Align.CENTER
        canvas.drawText(text, 0, text.length, 0f, height.toFloat(), p)
        bigBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        return Bitmap.createBitmap(brWidth, qrHeight, Bitmap.Config.ARGB_8888)
    }
}


/**
 * 解析二维码图片
 * @param path 路径
 * @return
 */
@JvmOverloads
fun scanningImage(path: String?): Result? {
    if (TextUtils.isEmpty(path)) {
        return null
    }
    val options: BitmapFactory.Options = BitmapFactory.Options()
    options.inJustDecodeBounds = true // 先获取原大小
    options.inJustDecodeBounds = false
    var sampleSize = (options.outHeight / 200)
    if (sampleSize <= 0) sampleSize = 1
    options.inSampleSize = sampleSize
    val scanBitmap: Bitmap = BitmapFactory.decodeFile(path, options)
    val data = IntArray(scanBitmap.width * scanBitmap.height)
    scanBitmap.getPixels(
        data, 0, scanBitmap.width, 0, 0, scanBitmap.width, scanBitmap.height
    )
    scanBitmap.getPixels(data, 0, scanBitmap.width, 0, 0, scanBitmap.width, scanBitmap.height)
    val rgbLuminanceSource = RGBLuminanceSource(scanBitmap.width, scanBitmap.height, data)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(rgbLuminanceSource))

    val multiFormatReader = MultiFormatReader()
// 解码的参数
//    // 解码的参数
    val hints = Hashtable<DecodeHintType, Any?>(2)
// 可以解析的编码类型
    // 可以解析的编码类型
    val decodeFormats = Vector<BarcodeFormat>()

// 扫描的类型  一维码和二维码
    decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS)
    decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS)
    decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS)

    hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
    // 设置解析配置参数
    multiFormatReader.setHints(hints)
    // 开始对图像资源解码
    var result: Result? = null
    try {
        result = multiFormatReader.decodeWithState(binaryBitmap)
    } catch (e: NotFoundException) {
        Log.e("hxy", "NotFoundException")
    } catch (e: ChecksumException) {
        Log.e("hxy", "ChecksumException")
    } catch (e: FormatException) {
        Log.e("hxy", "FormatException")
    }
    return result
}