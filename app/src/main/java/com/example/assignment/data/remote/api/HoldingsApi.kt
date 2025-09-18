package com.example.assignment.data.remote.api

import com.example.assignment.data.remote.dto.HoldingsResponse
import com.example.assignment.util.AppConstant
import retrofit2.http.GET

interface HoldingsApi {
    @GET(AppConstant.HOLDINGS)
    suspend fun getHoldings(): HoldingsResponse
}


