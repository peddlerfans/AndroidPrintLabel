package com.lnsk.deliver.page

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.hardware.print.jc.util.PrintUtil
import com.hardware.print.jc.util.PrintUtil.ERROR_MESSAGES
import com.lnsk.deliver.PrintLabel
import com.lnsk.deliver.api.model.OrderState
import com.lnsk.deliver.viewmodel.DeliverViewModel
import com.lnsk.deliver.viewmodel.UiState

@Composable
fun PrintList(navController: NavHostController, viewModel: DeliverViewModel) {
    LaunchedEffect(viewModel.seekingDevice) {
        Log.e("TAG", "$viewModel")
        if (viewModel.seekingDevice != null) {
            viewModel.init()
        }
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button({
                viewModel.resetDeliverUiState()
                navController.navigateUp()
            }, modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)) {
                Text("返回")
            }

            Button({
                viewModel.init()
            }, modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)) {
                Text("重新扫描设备")
            }

        }
        Row(
            Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Start // 设置水平排列方式为 Start
        ) {
            AsyncImage(
                viewModel.seekingDevice!!.image,
                "头像",
                Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(3.dp)),
                placeholder = null,
                error = null,
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier.padding(start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text("${viewModel.seekingDevice!!.productId}")
                Text(viewModel.seekingDevice!!.productName)
            }
            Button(
                onClick = {
                    viewModel.setOrderState(
                        OrderState(viewModel.orderNo)
                    )
                    Log.e("TAG1111111", "${viewModel.orderNo}")
                },
                modifier = Modifier
                    .padding(horizontal = 0.1.dp)
                    .size(200.dp, 60.dp) // 设置按钮的大小

            ) {
                Text("发货")
            }
        }
        PrintBitmapList(viewModel)
    }
}


@Composable
private fun PrintBitmapList(viewModel: DeliverViewModel) {
    when (val state = viewModel.seekDevice) {
        is UiState.Error -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(state.error.message ?: "error")
        }

        UiState.Loading -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("加载中")
        }

        is UiState.Success -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LazyColumn(Modifier.weight(1f)) {
                items(state.data) {
                    DeviceItem(it, viewModel)
                }
            }

            if (viewModel.showGenerateLabel) {
                when (val deliverState = viewModel.deliverUiState) {
                    is UiState.Error -> Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(deliverState.error.message ?: "error")
                    }

                    UiState.Loading -> Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("标签生成中")
                    }

                    is UiState.Success ->
                        Row {
                            val context = LocalContext.current
                            val bmp: Bitmap by remember {
                                mutableStateOf(
                                    Bitmap.createBitmap(
                                        320,
                                        240,
                                        Bitmap.Config.ARGB_8888
                                    )
                                )
                            }
                            PrintLabel(modifier = Modifier.weight(1f), deliverState.data, bmp)
                            Column {
                                var error by remember { mutableStateOf("") }
                                Text(error, Modifier.padding(vertical = 10.dp))
                                Button({
                                    PrintUtil.print(
                                        bmp, 0, 40f, 30f, 1
                                    ) { errorCode, printState ->
                                        error = ERROR_MESSAGES[errorCode] ?: "未知异常"

                                        Toast.makeText(
                                            context,
                                            ERROR_MESSAGES[errorCode],
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }) {
                                    Text("打印")
                                }
                            }
                        }


//                    LazyRow(
//                        modifier = Modifier.padding(5.dp),
//                        horizontalArrangement = Arrangement.spacedBy(
//                            10.dp
//                        )
//                    ) {
//                        items(state.data) {
//                            PrintBitmap(it)
//                        }
//                    }
                }
            }
        }
    }
}
