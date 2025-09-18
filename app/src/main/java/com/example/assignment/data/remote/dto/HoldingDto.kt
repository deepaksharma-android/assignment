package com.example.assignment.data.remote.dto

import com.example.assignment.domain.model.Holding
import com.google.gson.annotations.SerializedName

data class HoldingDto(
    @SerializedName("symbol") val symbol: String?="",
    @SerializedName("quantity") val quantity: Int?=0,
    @SerializedName("ltp") val ltp: Double?=0.0,
    @SerializedName("avgPrice") val avgPrice: Double?=0.0,
    @SerializedName("close") val close: Double?=0.0
) {
    fun toDomain(): Holding = Holding(
        symbol = symbol,
        quantity = quantity,
        lastTradedPrice = ltp,
        averagePrice = avgPrice,
        closePrice = close
    )
}


