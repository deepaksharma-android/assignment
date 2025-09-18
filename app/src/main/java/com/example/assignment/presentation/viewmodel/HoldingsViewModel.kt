package com.example.assignment.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.assignment.data.paging.HoldingsPagingSource
import com.example.assignment.domain.model.Holding
import com.example.assignment.domain.usecase.GetHoldingsUseCase
import com.example.assignment.presentation.model.Totals
import com.example.assignment.presentation.state.HoldingsUiState
import com.example.assignment.presentation.state.UiEvent
import com.example.assignment.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val getHoldings: GetHoldingsUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    // UI state exposed as a StateFlow for the Fragment to observe
    private val _uiState = MutableStateFlow(HoldingsUiState(isLoading = true))
    val uiState: StateFlow<HoldingsUiState> = _uiState.asStateFlow()

    // Holds current list of holdings to calculate summary totals
    private var accumulatedHoldings = mutableListOf<Holding>()

    // Pager handles pagination using Paging3 library
    private val pager: Pager<Int, Holding> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = {
            // Custom PagingSource that fetches holdings page by page via use case
            HoldingsPagingSource { page, pageSize -> getHoldings(page, pageSize) }
        }
    )

    // Flow of paginated data observed by UI (Fragment)
    val holdingsPaging: Flow<PagingData<Holding>> = pager.flow.cachedIn(viewModelScope)

    init {
        // Initial UI state can be adjusted here if needed
        _uiState.value = _uiState.value.copy(isLoading = false)
    }
//    init {
//        loadPagingData()
//    }
//
//    private fun loadPagingData() {
//        viewModelScope.launch(ioDispatcher) {
//            _uiState.update { it.copy(isLoading = true, error = null) }
//            accumulatedHoldings.clear()
//            holdingsPaging.collectLatest { pagingData ->
//                _uiState.update {
//                    it.copy(
//                        pagingData = pagingData,
//                        isLoading = false,
//                        error = null
//                    )
//                }
//            }
//        }
//    }

    /**
     * Called when new holdings data is loaded into the adapter snapshot.
     * Updates accumulated holdings and calculates summary totals for the UI.
     */
    fun updateHoldingSummaryFromSnapShot(newHoldings: List<Holding>) {
        accumulatedHoldings.clear()
        accumulatedHoldings.addAll(newHoldings)
        val totals = calculateTotals(accumulatedHoldings)
        _uiState.update {
            it.copy(
                totalCurrentValue = totals.totalCurrentValue,
                totalInvestment = totals.totalInvestment,
                totalPnL = totals.totalPnL,
                todaysPnL = totals.todaysPnL
            )
        }
    }

    /**
     * Calculates financial summary totals from list of holdings.
     */
    private fun calculateTotals(holdings: List<Holding>): Totals {
        var totalCurrentValue = 0.0
        var totalInvestment = 0.0
        var todaysPnL = 0.0

        holdings.forEach { h ->
            val qty = h.quantity ?: 0
            val ltp = h.lastTradedPrice ?: 0.0
            val avgPrice = h.averagePrice ?: 0.0
            val close = h.closePrice ?: 0.0

            totalCurrentValue += ltp * qty
            totalInvestment += avgPrice * qty
            todaysPnL += (close - ltp) * qty
        }

        val totalPnL = totalCurrentValue - totalInvestment

        return Totals(totalCurrentValue, totalInvestment, totalPnL, todaysPnL)
    }

}


