package com.lnsk.deliver.page


import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lnsk.deliver.api.bean.DeliverBean
import com.lnsk.deliver.api.model.Deliver
import com.lnsk.deliver.viewmodel.DeliverViewModel
import com.lnsk.deliver.viewmodel.UiState

/**
 * 设备列表
 * */
@Composable
fun DeviceItem(
    item: ScanResult,
    viewModel: DeliverViewModel,
) {
//    Log.e("TAG", "etOnPrintConnectionCallback${item}")
    Column(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(Color.White)
            .padding(5.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    item.device.address, style = TextStyle.Default.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(item.device.name ?: "error", Modifier)
            }


            Text("${item.rssi}", Modifier.padding(10.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button({
            viewModel.generateLabel(item.device,0)
                var type = 0
                if (item.device.name.contains("S9") || item.device.name.contains("YSZJ")) {
                    type = 1
                }
                if (item.device.name.contains("R12") || item.device.name.contains("R15")) {
                    type = 2
                }
                viewModel.setDeliver(DeliverBean(
                    address =item.device.address,
                    name =item.device.name,
                    type =type,
                    qrCode ="address:${item.device.address},name:${item.device.name},type:${type}",
                ))

            }) {
                Text("生成标签")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button({
                viewModel.generateLabel(item.device,1)
            }) {
                Text("生成订单标签")
            }
        }
    }
}
