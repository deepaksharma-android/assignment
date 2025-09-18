package com.example.assignment.domain.repository

import com.example.assignment.domain.model.Holding

interface HoldingsRepository {
    suspend fun getHoldings(page: Int, pageSize: Int): List<Holding>
}


