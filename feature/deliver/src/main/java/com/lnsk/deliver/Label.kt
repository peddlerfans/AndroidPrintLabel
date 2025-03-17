package com.lnsk.deliver

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hardware.print.jc.util.ImgUtil
import com.lnsk.deliver.api.bean.DeliverBean
import kotlin.math.max
import kotlin.math.min


@Preview
@Composable
fun PrintLabelPreview() {
    val deliverBean by remember {
        mutableStateOf(
            DeliverBean(
                address = "FF:44:03:34:31:F6",
//                color = "银灰色",
                name = "Ccarbon-S9-12790",
//                size = "40",
//                style = "女款",
                type = 1,
                qrCode = "orderNo:NO202412302112072375455,address:FF:44:03:34:31:F6,type:1"
            )
        )
    }
    PrintLabel(
        deliverBean = deliverBean,
    )
}

@Preview
@Composable
fun PrintLabelPreview2() {
    val deliverBean by remember {
        mutableStateOf(
            DeliverBean(
                address = "FF:44:03:34:31:F6",
                color = "银灰色",
                name = "Ccarbon-R15Pro",
                size = "40",
                style = "",
                type = 2,
                qrCode = "orderNo:NO202412302112072375455,address:FF:44:03:34:31:F6,type:1"
            )
        )
    }
    PrintLabel(
        deliverBean = deliverBean,
    )
}


@Preview
@Composable
fun PrintLabel(
    modifier: Modifier = Modifier, deliverBean: DeliverBean = DeliverBean(
        address = "FF:44:03:34:31:F6",
        color = "黑尾",
        name = "Ccarbon-S9-12790",
        size = "40",
        style = "女款",
        type = 1,
        qrCode = "orderNo:NO202412302112072375455,address:FF:44:03:34:31:F6,type:1"
    ), bmp: Bitmap = Bitmap.createBitmap(
        320, 240, Bitmap.Config.ARGB_8888
    )
) {
    val canvas = Canvas(bmp)

    val paint = TextPaint().apply {
        textSize = 32f
        color = Color.BLACK
        isAntiAlias = false
        isDither = false
        hinting = Paint.ANTI_ALIAS_FLAG
        isFakeBoldText = true // true 代表启用加粗
    }

    canvas.drawColor(Color.WHITE)

    // 绘制多行文本
    var startY = 26 // 初始 y 坐标
    var startX = 15f //canvas.width / 2f // 水平居中
    val verticalSpacing = 18f // 行间距

    var rect = Rect().apply {
        bottom = startY
    }
    // 绘制多行文本并获取每行文本的边界矩形
    rect = drawTextWithSpacing(
        canvas, deliverBean.address, startX, rect.bottom.toFloat(), paint.apply {
            textSize = 32f
            isFakeBoldText = true
        }, verticalSpacing, centerX = canvas.width / 2f
    )

//    startX = rect.left.toFloat()

    rect = drawTextWithSpacing(
        canvas, deliverBean.name, startX, rect.bottom.toFloat(), paint.apply {
            textSize = 26f
            isFakeBoldText = false
        }, verticalSpacing, centerX = canvas.width / 2f
    )

    startY = rect.bottom

    if (deliverBean.style.isNotEmpty()){
        if (deliverBean.type != 2) {
            rect = drawTextWithSpacing(
                canvas,
                deliverBean.style,
                startX,
                startY.toFloat(),
                paint.apply {
                    textSize = 40f
                    isFakeBoldText = true
                },
                verticalSpacing,
            )
        }
    }

    if (deliverBean.size.isNotEmpty()){
        val size = if (deliverBean.type == 1) "${deliverBean.size}码" else "尺寸:${deliverBean.size}";
        rect = drawTextWithSpacing(
            canvas,
            size,
            if (deliverBean.type == 1) rect.right.toFloat() + 8f else startX,
            startY.toFloat() + 28f,
            paint.apply {
                textSize = 26f
                isFakeBoldText = false
            },
            verticalSpacing,
        )
    }

    var qrCodeLeft: Int = rect.right
    var qrCodeLeft1: Int = rect.right

    val qrCodeTop = startY - 28
    if (deliverBean.color.isNotEmpty()) {
        rect = drawTextWithSpacing(
            canvas, deliverBean.color, startX = startX, rect.bottom.toFloat(), paint.apply {
                textSize = 48f
                isFakeBoldText = false
            }, verticalSpacing
        )
    }
    if (deliverBean.type == 1) {
        qrCodeLeft = rect.right
    }
    qrCodeLeft = max(max(qrCodeLeft, rect.right),qrCodeLeft1)

    if (deliverBean.color.isEmpty() && deliverBean.size.isEmpty()&& deliverBean.style.isEmpty()){
        qrCodeLeft = 0
    }
    // 二维码的大小
    val left = qrCodeLeft + 10
    val top = qrCodeTop + 10


    val w = canvas.width - left
    val h = canvas.height - top


    val qrCodeSize = min(w, h)
//
    val right = left + qrCodeSize
    val bottom = top + qrCodeSize
    val qrCodeRect = Rect(left, top, right, bottom)
    canvas.drawBitmap(
        deliverBean.qrCode.createQRCode(qrCodeRect.right - qrCodeRect.left),
        qrCodeRect.left.toFloat(),
        qrCodeRect.top.toFloat(),
        paint
    )

    Image(
        bmp.asImageBitmap(),
        "",
        modifier.height(200.dp).fillMaxWidth().background(androidx.compose.ui.graphics.Color.Red)
    )
}

/**
 * 在指定位置绘制文本，并返回下一行文本的起始 x, y 坐标以及文本的边界矩形。
 *
 * @param canvas         用于绘制的 Canvas 对象
 * @param text           要绘制的文本
 * @param centerX        文本的中心 x 坐标
 * @param startY         文本的起始 y 坐标
 * @param paint          用于绘制文本的 Paint 对象
 * @param verticalSpacing 行间距
 * @return 包含文本边界矩形和下一行文本起始坐标的 Rect 对象
 */
private fun drawTextWithSpacing(
    canvas: Canvas,
    text: String,
    startX: Float,
    startY: Float,
    paint: Paint,
    verticalSpacing: Float,
    centerX: Float = 0f,
    isCenter: Boolean = centerX > 0f
): Rect {
    // 创建一个 Rect 来保存文本的边界信息
//    val textBounds = Rect()

    // 计算文本的宽度和高度
    val textWidth: Float = ImgUtil.getFontWidth(paint, text).toFloat()
    val textHeight: Float = ImgUtil.getFontHeight(paint, text).toFloat()

    // 计算文本的 x 和 y 坐标
    // 居中对齐
    val x = if (isCenter) centerX - textWidth / 2 else startX
    val y = startY + textHeight

    // 绘制文本
    canvas.drawText(text, x, y, paint)

    // 设置文本的边界矩形
    val textBounds = Rect().apply {
        left = x.toInt()
        top = (startY).toInt()
        right = (x + textWidth).toInt()
        bottom = (verticalSpacing + y).toInt()
    }
    // 返回下一行文本的起始坐标作为 Rect 的额外属性
    // 注意：Rect 通常用于整数坐标，这里我们将浮点值转换为整数
    // 如果需要更精确的坐标，可以考虑使用 RectF
    return textBounds
}