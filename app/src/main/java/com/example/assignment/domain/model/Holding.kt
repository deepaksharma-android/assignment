package com.example.assignment.domain.model

data class Holding(
    val symbol: String?,
    val quantity: Int?=0,
    val lastTradedPrice: Double?,
    val averagePrice: Double?,
    val closePrice: Double?
)


