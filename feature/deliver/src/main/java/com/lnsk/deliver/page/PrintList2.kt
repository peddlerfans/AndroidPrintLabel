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
import com.lnsk.deliver.viewmodel.DeliverViewModel
import com.lnsk.deliver.viewmodel.UiState
import kotlin.math.log

@Composable
fun PrintList2(navController: NavHostController, viewModel: DeliverViewModel) {
    LaunchedEffect(Unit) {
        viewModel.init()
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

        PrintBitmapList(viewModel)
    }
}


@Composable
private fun PrintBitmapList(viewModel: DeliverViewModel) {
//    Log.e("TAG","setOnPrint:${viewModel.seekDevice}")
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
                }
            }
        }
    }
}
