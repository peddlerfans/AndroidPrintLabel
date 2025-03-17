package com.lnsk.deliver.api.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class OrderBean(
    @SerializedName("create_time")
    val createTime: String,
    val freight: Int,
    val id: Int,
    val method: Int,
//    @SerializedName("order_no")
    val orderNo: String,
    @SerializedName("order_num")
    val orderNum: Int,
//    @SerializedName("order_number")
    val orderNumber: Int,
    @SerializedName("pay_amount")
    val payAmount: Int,
    @SerializedName("pay_time")
    val payTime: String,
    @SerializedName("pay_token")
    val payToken: String,
    @SerializedName("pay_type")
    val payType: Int,
    @SerializedName("payment_name")
    val paymentName: String,
    @SerializedName("payment_order_number")
    val paymentOrderNumber: String,
//    @SerializedName("product_content")
    val productContent: List<ProductContent>?,
    @SerializedName("redeem_code")
    val redeemCode: String,
    val remark: String,
    @SerializedName("remark_s")
    val remarkS: String,
    @SerializedName("share_commision")
    val shareCommision: Int,
    @SerializedName("share_state")
    val shareState: Int,
    val state: Int,
    @SerializedName("take_address")
    val takeAddress: String,
    @SerializedName("take_order")
    val takeOrder: Int,
    @SerializedName("token_amount")
    val tokenAmount: String,
    @SerializedName("token_id")
    val tokenId: Int,
    @SerializedName("token_rate")
    val tokenRate: String,
    @SerializedName("total_amount")
    val totalAmount: Int,
    @SerializedName("tracking_number")
    val trackingNumber: String,
    val tx: String,
    @SerializedName("user_id")
    val userId: Int,
    val virtual: Int
) : Serializable

data class ProductContent(
    val color: String,
    val desc: String,
    @SerializedName("en_color")
    val enColor: String,
    @SerializedName("en_name")
    val enName: String,
    @SerializedName("en_product_name")
    val enProductName: String,
    @SerializedName("ft_color")
    val ftColor: String,
    @SerializedName("ft_name")
    val ftName: String,
    @SerializedName("ft_product_name")
    val ftProductName: String,
    val id: Int,
    val image: String,
    val name: String,
    val number: Int,
    val power: Int,
    val price: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("product_name")
    val productName: String,
    val size: String,
    val stock: Int
)




