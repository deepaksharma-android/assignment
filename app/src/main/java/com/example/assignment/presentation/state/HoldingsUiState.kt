package com.example.assignment.presentation.state

import androidx.paging.PagingData
import androidx.paging.PagingData.Companion.empty
import com.example.assignment.domain.model.Holding

data class HoldingsUiState(
	val isLoading: Boolean = false,
	val pagingData: PagingData<Holding> = empty(),
	val totalCurrentValue: Double = 0.0,
	val totalInvestment: Double = 0.0,
	val totalPnL: Double = 0.0,
	val todaysPnL: Double = 0.0,
	val error: String? = null
)


