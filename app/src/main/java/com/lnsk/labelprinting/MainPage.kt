package com.lnsk.labelprinting


import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hardware.print.jc.util.PrintUtil
import com.lnsk.deliver.PrintLabel
import com.lnsk.deliver.navigation.DeliverRouter
import com.lnsk.deliver.page.DeliverPage
import com.lnsk.deliver.page.PrintList
import com.lnsk.deliver.page.PrintList2
import com.lnsk.deliver.page.ProductContentPage
import com.lnsk.deliver.page.SearchScannerPage
import com.lnsk.deliver.viewmodel.DeliverViewModel
import com.lnsk.labelprinting.ui.theme.LabelPrintingTheme

@Preview
@Composable
fun MainPage(navController: NavHostController = rememberNavController()) {
    LabelPrintingTheme {
        val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
        val viewModel: DeliverViewModel = viewModel()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(snackBarHostState, modifier = Modifier) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Snackbar(
                            snackbarData = it,
                            shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp),
//                            shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
                            containerColor = SnackbarDefaults.color.copy(alpha = 0.6f), // 背景颜色
                        )
                    }
                }
            },
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = DeliverRouter.DELIVER,
//                    startDestination = "test",
                ) {
//                    composable("test") {
//                        Column {
//                            Button({
//                                PrintUtil.connectBluetoothPrinter("28:0B:12:E9:C4:87")
//                            }) {
//                                Text("连接:28:0B:12:E9:C4:87")
//                            }
//                            Row {
//                                val context = LocalContext.current
//                                val bmp: Bitmap = Bitmap.createBitmap(
//                                    320,
//                                    240,
//                                    Bitmap.Config.ARGB_8888
//                                )
//                                PrintLabel(bmp = bmp)
//                                Button({
//                                    PrintUtil.print(
//                                        bmp, 0, 40f, 30f, 1, 0, 0, 0, 0, ""
//                                    )
//                                }) {
//                                    Text("打印")
//                                }
//                            }
//                        }
//                    }
                    composable(DeliverRouter.DELIVER) {
                        // 订单
                        DeliverPage(navController, viewModel)
                    }
                    composable(DeliverRouter.PRODUCT_CONTENT) {
                        // 订单
                        ProductContentPage(navController, viewModel)
                    }
                    composable(DeliverRouter.PRINT_LIST) {
                        // 订单
                        PrintList(navController, viewModel)
                    }
                    composable(DeliverRouter.PRINT_LIST2) {
                        // 订单2
                        PrintList2(navController, viewModel)
                    }
                    composable(DeliverRouter.SEARCH_SCANNER) {
                        // 订单
                        SearchScannerPage()
                    }
                }
            }

        }
    }
}
