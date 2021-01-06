package com.demon.activitychange.app

data class YellowPage(
    val address: String,
    val createdAt: String,
    val id: String,
    val latitude: String,
    val longitude: String,
    val mobile: String,
    val pid: String,
    val sort: Int,
    val title: String,
    val updatedAt: String
)

data class Idle(
    val address: String,
    val avatar: String,
    val classifyText: String,
    val content: String,
    val createdAt: String,
    val createdBy: String,
    val extJson: String,
    val id: String,
    val idCode: String,
    val imgs: String,
    val labels: String,
    val labelsids: String,
    val linkman: String,
    val linkmanphone: String,
    val price: String,
    val title: String,
    val userId: String,
    val userName: String
)
data class idleClassify(
    val child: List<Any>,
    val createdAt: String,
    val id: String,
    val isShow: Int,
    val name: String,
    val parentId: String,
    val serviceName: String,
    val sort: Int,
    val updatedAt: String
)
data class UsedCar(
    val author: String,
    val avatar: String,
    val classifyCode: String,
    val commentSum: Int,
    val content: String,
    val coverImg: String,
    val createdAt: String,
    val id: String,
    val idCode: String,
    val imgs: String,
    val likeSum: Int,
    val readSum: Int,
    val status: Int,
    val title: String,
    val userId: String,
    val userName: String,
    val video: String,
    val videoImg: String
)
data class circle(
    val author: String,
    val avatar: String,
    val classifyCode: String,
    val commentSum: Int,
    val content: String,
    val coverImg: String,
    val createdAt: String,
    val id: String,
    val idCode: String,
    val imgs: String,
    val likeSum: Int,
    val readSum: Int,
    val status: Int,
    val title: String,
    val userId: String,
    val userName: String,
    val video: String,
    val videoImg: String
)