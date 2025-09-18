package com.example.assignment.presentation.viewmodel

import com.example.assignment.domain.model.Holding
import com.example.assignment.domain.usecase.GetHoldingsUseCase
import com.example.assignment.presentation.model.Totals
import com.example.assignment.test.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class HoldingsViewModelSummaryTest : BaseTest() {

    private lateinit var vm: HoldingsViewModel

    @Before
    fun setup() {
        vm = HoldingsViewModel(
            getHoldings = mock<GetHoldingsUseCase>(),
            ioDispatcher = dispatcher
        )
    }

    @Test
    fun updateHoldingSummaryFromSnapShot_computesTotals() = runTest(dispatcher) {
        val items = listOf(
            Holding("AAA", 2, 100.0, 90.0, 98.0),
            Holding("BBB", 1, 200.0, 150.0, 190.0)
        )
        vm.updateHoldingSummaryFromSnapShot(items)
        val s = vm.uiState.value
        assertEquals(400.0, s.totalCurrentValue, 0.0)
        assertEquals(330.0, s.totalInvestment, 0.0)
        assertEquals(70.0, s.totalPnL, 0.0)
        assertEquals(-14.0, s.todaysPnL, 0.0)
    }

    @Test
    fun updateHoldingSummaryFromSnapShot_empty_setsZero() = runTest(dispatcher) {
        vm.updateHoldingSummaryFromSnapShot(emptyList())
        val s = vm.uiState.value
        assertEquals(0.0, s.totalCurrentValue, 0.0)
        assertEquals(0.0, s.totalInvestment, 0.0)
        assertEquals(0.0, s.totalPnL, 0.0)
        assertEquals(0.0, s.todaysPnL, 0.0)
    }

    @Test
    fun updateHoldingSummaryFromSnapShot_handlesNullFieldsAsZero() = runTest(dispatcher) {
        val items = listOf(
            Holding(symbol = "NULLS", quantity = null, lastTradedPrice = null, averagePrice = null, closePrice = null),
            Holding(symbol = "MIX", quantity = 3, lastTradedPrice = 0.0, averagePrice = 40.0, closePrice = 50.0)
        )

        vm.updateHoldingSummaryFromSnapShot(items)

        val s = vm.uiState.value
        val expectedCurrent = 0.0 + (3 * 0.0)
        val expectedInvestment = 0.0 + (3 * 40.0)
        val expectedPnL = expectedCurrent - expectedInvestment
        val expectedTodays = (0.0 - 0.0) * 0 + (50.0 - 0.0) * 3

        assertEquals(expectedCurrent, s.totalCurrentValue, 0.0)
        assertEquals(expectedInvestment, s.totalInvestment, 0.0)
        assertEquals(expectedPnL, s.totalPnL, 0.0)
        assertEquals(expectedTodays, s.todaysPnL, 0.0)
    }

    @Test
    fun updateHoldingSummaryFromSnapShot_zeroQuantity_noEffect() = runTest(dispatcher) {
        val items = listOf(
            Holding(symbol = "ZERO", quantity = 0, lastTradedPrice = 999.0, averagePrice = 888.0, closePrice = 777.0)
        )

        vm.updateHoldingSummaryFromSnapShot(items)

        val s = vm.uiState.value
        assertEquals(0.0, s.totalCurrentValue, 0.0)
        assertEquals(0.0, s.totalInvestment, 0.0)
        assertEquals(0.0, s.totalPnL, 0.0)
        assertEquals(0.0, s.todaysPnL, 0.0)
    }

    @Test
    fun updateHoldingSummaryFromSnapShot_mixedGainsAndLosses() = runTest(dispatcher) {
        val items = listOf(
            // Gain: ltp > avgPrice, close > ltp
            Holding(symbol = "GAIN", quantity = 2, lastTradedPrice = 120.0, averagePrice = 100.0, closePrice = 130.0),
            // Loss: ltp < avgPrice, close < ltp
            Holding(symbol = "LOSS", quantity = 3, lastTradedPrice = 80.0, averagePrice = 90.0, closePrice = 70.0)
        )

        vm.updateHoldingSummaryFromSnapShot(items)

        val s = vm.uiState.value
        val expectedCurrent = (2 * 120.0) + (3 * 80.0)           // 240 + 240 = 480
        val expectedInvestment = (2 * 100.0) + (3 * 90.0)        // 200 + 270 = 470
        val expectedPnL = expectedCurrent - expectedInvestment   // 10
        val expectedTodays = (130.0 - 120.0) * 2 + (70.0 - 80.0) * 3 // 20 - 30 = -10

        assertEquals(expectedCurrent, s.totalCurrentValue, 0.0)
        assertEquals(expectedInvestment, s.totalInvestment, 0.0)
        assertEquals(expectedPnL, s.totalPnL, 0.0)
        assertEquals(expectedTodays, s.todaysPnL, 0.0)
    }

    // ===== Direct tests for calculateTotals (via reflection) =====

    private fun callCalculateTotals(items: List<Holding>): Totals {
        val method = HoldingsViewModel::class.java.getDeclaredMethod("calculateTotals", List::class.java)
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return method.invoke(vm, items) as Totals
    }

    @Test
    fun calculateTotals_nonNullValues_returnsExpected() = runTest(dispatcher) {
        val items = listOf(
            Holding("AAA", 2, 100.0, 90.0, 98.0),
            Holding("BBB", 1, 200.0, 150.0, 190.0)
        )
        val t = callCalculateTotals(items)
        assertEquals(400.0, t.totalCurrentValue, 0.0)
        assertEquals(330.0, t.totalInvestment, 0.0)
        assertEquals(70.0, t.totalPnL, 0.0)
        assertEquals(-14.0, t.todaysPnL, 0.0)
    }

    @Test
    fun calculateTotals_handlesNullFieldsAsZero() = runTest(dispatcher) {
        val items = listOf(
            Holding(symbol = "NULLS", quantity = null, lastTradedPrice = null, averagePrice = null, closePrice = null),
            Holding(symbol = "MIX", quantity = 3, lastTradedPrice = 0.0, averagePrice = 40.0, closePrice = 50.0)
        )
        val t = callCalculateTotals(items)
        val expectedCurrent = 0.0 + (3 * 0.0)
        val expectedInvestment = 0.0 + (3 * 40.0)
        val expectedPnL = expectedCurrent - expectedInvestment
        val expectedTodays = (0.0 - 0.0) * 0 + (50.0 - 0.0) * 3
        assertEquals(expectedCurrent, t.totalCurrentValue, 0.0)
        assertEquals(expectedInvestment, t.totalInvestment, 0.0)
        assertEquals(expectedPnL, t.totalPnL, 0.0)
        assertEquals(expectedTodays, t.todaysPnL, 0.0)
    }

    @Test
    fun calculateTotals_zeroQuantity_noEffect() = runTest(dispatcher) {
        val items = listOf(
            Holding(symbol = "ZERO", quantity = 0, lastTradedPrice = 999.0, averagePrice = 888.0, closePrice = 777.0)
        )
        val t = callCalculateTotals(items)
        assertEquals(0.0, t.totalCurrentValue, 0.0)
        assertEquals(0.0, t.totalInvestment, 0.0)
        assertEquals(0.0, t.totalPnL, 0.0)
        assertEquals(0.0, t.todaysPnL, 0.0)
    }
}


