package com.example.assignment.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HoldingsResponse(
    @SerializedName("data") val data: HoldingsData?=null
)

data class HoldingsData(
    @SerializedName("userHolding") val userHolding: List<HoldingDto>?= arrayListOf()
)


