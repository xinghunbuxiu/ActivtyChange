package com.demon.activitychange.bean

import java.io.Serializable

data class OrderBean(
    val commInfo: String,
    val orderNum: String,
    val orderTime: String,
    val orderTimeStamp: Long,
    val totalPrice: Double,
    val userName: String,
    var imageUrl: String?=null
) : Serializable, Comparable<OrderBean> {
    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     */
    override fun compareTo(other: OrderBean): Int {
        return other.orderTimeStamp.compareTo(this.orderTimeStamp)
    }

}