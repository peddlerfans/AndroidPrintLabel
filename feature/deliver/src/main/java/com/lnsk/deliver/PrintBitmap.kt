//package com.lnsk.deliver
//
//import android.content.Context
//import android.util.DisplayMetrics
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.lnsk.deliver.api.bean.DeliverBean
//
//
//val Int.mm: Dp get() = (this / 25.4f * 160f).dp
//val Double.mm: Dp get() = (this / 25.4f * 160f).dp
//val Double.mmsp: TextUnit get() = (this / 25.4f * 160f).sp
//val Int.mmsp: TextUnit get() = (this / 25.4f * 160f).sp
//
//fun Int.mm(context: Context): Dp {
//    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
//    val dpi = displayMetrics.densityDpi
//    Log.e("TAG", "$dpi")
//    val px = this * ((dpi / 25.4))
//    return (px / (dpi / 160)).dp
//}
//
//@Preview
//@Composable
//fun PrintBitmap(deliverBean: DeliverBean = DeliverBean()) {
//    val context = LocalContext.current
//    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
//    val dpi = displayMetrics.densityDpi
//    Row(
//        modifier = Modifier
//            .background(Color.White, RoundedCornerShape(10.dp))
//            .size(40.mm(context), 30.mm(context))
//            .padding(1.mm(context)),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(
//            modifier = Modifier.weight(1f),
////            verticalArrangement = Arrangement.spacedBy(2.8.mm),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            //$dpi
//            Text(
//                deliverBean.address,
//                modifier = Modifier,
//                style = TextStyle.Default.copy(
//                    fontSize = 4.mmsp,
//                    textAlign = TextAlign.Center,
//                    fontWeight = FontWeight.Bold
//                )
//            )
//
//            Row(
////                horizontalArrangement = Arrangement.spacedBy(5.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth().padding(5.dp)
//            ) {
//                Text(deliverBean.name, fontSize = 3.2.mmsp)
////                Text("ccarbon", fontSize = 3.2.mmsp)
////                Text("R15Pro", fontSize = 3.2.mmsp)
////                Text("12790", fontSize = 3.2.mmsp)
//            }
//
//            Row(Modifier.weight(1f)) {
//                Column(horizontalAlignment = Alignment.Start) {
//                    Spacer(Modifier.weight(1f))
//                    Row(
//                        verticalAlignment = Alignment.Bottom,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            deliverBean.style, fontSize = 4.8.mmsp,
//                            style = TextStyle.Default.copy(
//                                fontWeight = FontWeight.Bold
//                            )
//                        )
//                        Text(deliverBean.size, fontSize = 2.8.mmsp, modifier = Modifier.padding(start = 7.dp))
//                    }
//                    Spacer(Modifier.weight(1f))
//                    Text(
//                        deliverBean.color, fontSize = 5.8.mmsp, style = TextStyle.Default.copy(
//                            fontWeight = FontWeight.Bold
//                        )
//                    )
//                }
//                Spacer(Modifier.weight(1f))
//                Row(Modifier.size(14.5.mm).background(Color.Red).align(Alignment.Bottom)) {
//
//                }
////                Image(
////                    painterResource(R.drawable.ic_launcher_background), null,
////                    Modifier.size(14.5.mm).padding(start = 2.mm(context))
////                        .background(Color(0XFFCCCCCC))
////                )
//            }
//
//        }
//    }
//}