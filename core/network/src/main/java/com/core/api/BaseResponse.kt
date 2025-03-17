package com.core.api

const val CODE_SUCCESS = 0          //请求成功

/**
 * date:        2021/9/9 0009
 * author:      Bill
 * describe:    返回JSON统一数据
 */
data class BaseResponse<T>(val success: Boolean, val code: Int, val msg: String, val data: T) {
    fun isSuccess(): Boolean {
        return code == CODE_SUCCESS
    }
}
