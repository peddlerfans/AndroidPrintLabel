package com.lnsk.deliver.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hardware.print.jc.util.PrintUtil
import com.lnsk.deliver.api.bean.OrderBean
import com.lnsk.deliver.navigation.DeliverRouter.PRINT_LIST
import com.lnsk.deliver.navigation.DeliverRouter.PRINT_LIST2
import com.lnsk.deliver.navigation.DeliverRouter.PRODUCT_CONTENT
import com.lnsk.deliver.viewmodel.DeliverViewModel
import com.lnsk.deliver.viewmodel.UiState

/**
 * 发货列表
 * */
@Preview
@Composable
fun DeliverPage(
    navController: NavHostController = rememberNavController(),
    viewModel: DeliverViewModel = viewModel(),
) {
    var color by remember { mutableStateOf(Color.Red) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                PrintUtil.setOnPrintConnectionCallback {
                    Log.e("TAG","setOnPrintConnectionCallback:$it")
                    color = if (PrintUtil.isConnection()) Color.Blue else Color.Red
                }
                viewModel.getOrderList()

//                if (!PrintUtil.isConnection()) {
//                    PrintUtil.connectBluetoothPrinter("12:28:0B:E9:C4:87")
//                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    Column {
        val uiState = viewModel.orderUiState

        Row(
            Modifier.padding(horizontal = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Button({
//                navController.navigate(SEARCH_SCANNER)
//            }) {
//                Text("连接")
//            }


            Box(Modifier.size(18.dp).background(color))

            Button({
//                PrintUtil.connectBluetoothPrinter("28:0B:12:E9:C4:87")
                PrintUtil.connectBluetoothPrinter("12:28:0B:E9:C4:87")
            }) {
                Text("连接到打印机")
            }

            Button(
                {
//                    color = if (PrintUtil.isConnection()) Color.Blue else Color.Red
                    viewModel.getOrderList()
                },
                enabled = uiState !is UiState.Loading
            ) {
                Text("刷新")
            }
            Button(
                {
//                    color = if (PrintUtil.isConnection()) Color.Blue else Color.Red
                    navController.navigate(PRINT_LIST2)
                },
                enabled = uiState !is UiState.Loading
            ) {
                Text("查找设备")
            }
        }

        when (uiState) {
            is UiState.Error ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text("${uiState.error}")
                }

            UiState.Loading ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text("加载中")
                }

            is UiState.Success -> {
                if (uiState.data.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text("暂无数据")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(uiState.data) {
                            DeliverItem(it, modifier = Modifier.clickable {
                                viewModel.upDeliverData(
                                    it.orderNo,
                                    it.productContent ?: arrayListOf()
                                )
                                navController.navigate(PRODUCT_CONTENT)
                            })
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun DeliverItem(orderBean: OrderBean, modifier: Modifier = Modifier) {
    Column(
        Modifier.clip(RoundedCornerShape(8.dp))
            .then(modifier)
            .background(Color.White)
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text("订单编号:${orderBean.orderNo}")
        Text("购买数量:${orderBean.orderNumber}")
        Text("实体设备数量:${orderBean.productContent?.size ?: 0}")
    }
}

