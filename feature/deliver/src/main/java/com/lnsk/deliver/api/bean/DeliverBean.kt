package com.lnsk.deliver.api.bean


data class DeliverBean(
    val address: String = "",
    val color: String = "",
    val name: String = "",
    val size: String = "",
    val style: String = "",
    val type: Int = -1,
    val qrCode: String = ""
)
