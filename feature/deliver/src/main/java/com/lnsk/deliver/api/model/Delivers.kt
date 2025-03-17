package com.lnsk.deliver.api.model


data class Deliver(
    val orderNo: String?,
    val address: String,
    val name: String,
    val skuId: Int = 0, // 添加默认值
    val type: Int = 0  // 添加默认值
)


data class Delivers(
    val orderNo: String,
    val devices: List<Device>
)

data class Device(
    val address: String,
    val name: String,
    val skuId: Int,
    val type: Int
)


