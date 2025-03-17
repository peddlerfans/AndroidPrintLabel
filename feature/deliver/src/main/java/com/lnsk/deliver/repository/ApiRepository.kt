package com.lnsk.deliver.repository

import com.core.api.Api
import com.core.api.BaseResponse
import com.lnsk.deliver.api.ApiService
import com.lnsk.deliver.api.bean.DeliverBean
import com.lnsk.deliver.api.bean.OrderBean
import com.lnsk.deliver.api.model.Deliver

object ApiRepository {
    private val service by lazy {
//        Api.create<ApiService>(url = "https://apifoxmock.com/m1/5699091-5380170-default/")
//        Api.create<ApiService>(url = "http://192.168.31.56:8000")
        // 测试地址
//        Api.create<ApiService>(url = "http://en_depin_admin.youyong.org.cn")

        // 正式地址
        Api.create<ApiService>(url = "https://admin.ccarbon.online")
    }


    suspend fun orderList(): Result<BaseResponse<List<OrderBean>>> {
        return kotlin.runCatching {
            service.orderList(size = 30)
        }
    }
//
//    suspend fun deliverAdd(deliver: Delivers): Result<BaseResponse<List<DeliverBean>>> {
//        return kotlin.runCatching {
//            service.deliverAddAll(deliver)
//        }
//    }

    suspend fun getLabelInfo(deliver: Deliver): Result<BaseResponse<DeliverBean>> {
        return kotlin.runCatching {
            service.getLabelInfo(deliver)
        }
    }
}