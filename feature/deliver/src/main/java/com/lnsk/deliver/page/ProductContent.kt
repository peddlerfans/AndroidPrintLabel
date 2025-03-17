package com.lnsk.deliver.page

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.lnsk.deliver.api.bean.ProductContent
import com.lnsk.deliver.navigation.DeliverRouter.PRINT_LIST
import com.lnsk.deliver.viewmodel.DeliverViewModel

@Preview
@Composable
fun ProductContentPage(
    navController: NavHostController = rememberNavController(),
    viewModel: DeliverViewModel = viewModel(),
) {
    val deliverData by viewModel.deliverDataState.collectAsState()

    LazyColumn(Modifier.fillMaxSize()) {
        items(deliverData) { item ->
            ProductItem(navController, item, viewModel)
        }
    }
}

/**
 * 产品item
 * */
@Composable
private fun ProductItem(
    navController: NavHostController,
    item: ProductContent,
    viewModel: DeliverViewModel
) {
    Row(
        Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)
    ) {
        AsyncImage(
            item.image,
            "头像",
            Modifier.size(55.dp)
                .clip(RoundedCornerShape(3.dp)),
            placeholder = null,
            error = null,
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text("${item.productId}", Modifier)
                Text("尺寸：${item.size}", Modifier.weight(1f))
            }
            Text(item.productName)

            val context = LocalContext.current
            Button({
                Toast.makeText(context, "查找设备", Toast.LENGTH_LONG).show()
                viewModel.seekingDevice = item
                navController.navigate(PRINT_LIST)
            }) {
                Text("查找设备")
            }
        }

        Spacer(Modifier.weight(1f))
    }
}



