package com.lnsk.deliver.api

import com.core.api.BaseResponse
import com.lnsk.deliver.api.bean.DeliverBean
import com.lnsk.deliver.api.bean.OrderBean
import com.lnsk.deliver.api.model.Deliver
import com.lnsk.deliver.api.model.OrderState
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    /**
     * @param state 非必传  订单状态  0:待支付 1:已支付 2:已发货 3:已收货 8:申请退款 9:已取消 10:已退款
     * */
//    @GET("/api/order/lists")
    @FormUrlEncoded
    @POST("manage/order/lists")
    suspend fun orderList(
        @Field("state") state: Int = 1,
        @Field("page") page: Int = 1,
        @Field("size") size: Int = 10,
//        @Field("remark") remark: String = "领取设备",
    ): BaseResponse<List<OrderBean>>

//    /**
//     * @param deliver 添加发货交付
//     * */
//    @POST("/api/deliver/addAll")
//    suspend fun deliverAddAll(@Body deliver: Delivers): BaseResponse<List<DeliverBean>>

    @POST("manage/deliver/getLabelInfo")
    suspend fun getLabelInfo(@Body deliver: Deliver): BaseResponse<DeliverBean>

    @POST("/manage/order/send")
    suspend fun setOrderState(@Body deliver: OrderState): BaseResponse<DeliverBean>
}