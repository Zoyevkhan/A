package com.tv9news.models.masterHit

data class MasterApi(
    val status: Boolean,
    val message: String,
    val data: DataMaster,
    val time: Int,
    val interval: Int,
    val limit: Int,
    val cd_time: Long
)
