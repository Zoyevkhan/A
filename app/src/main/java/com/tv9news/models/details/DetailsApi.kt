package com.tv9news.models.details

import com.tv9news.models.masterHit.DataMaster

data class DetailsApi(
    val status: Boolean,
    val message: String,
    val data: DataDetails,
)