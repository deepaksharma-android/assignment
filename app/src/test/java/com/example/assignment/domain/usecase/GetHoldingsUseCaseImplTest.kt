package com.example.assignment.domain.usecase

import com.example.assignment.domain.model.Holding
import com.example.assignment.domain.repository.HoldingsRepository
import com.example.assignment.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetHoldingsUseCaseImplTest : BaseTest() {

    private lateinit var repo: HoldingsRepository
    private lateinit var useCase: GetHoldingsUseCase

    private val all = listOf(
        Holding("AAA", 2, 100.0, 90.0, 98.0),
        Holding("BBB", 1, 200.0, 150.0, 190.0),
        Holding("CCC", 3, 50.0, 40.0, 45.0),
        Holding("DDD", 5, 10.0, 8.0, 9.5)
    )

    @Before
    fun setupUseCase() {
        repo = mock()
        useCase = GetHoldingsUseCaseImpl(repo)
    }

    @Test
    fun `invoke returns repository data for any page args`() = kotlinx.coroutines.test.runTest(dispatcher) {
        whenever(repo.getHoldings(org.mockito.kotlin.any(), org.mockito.kotlin.any())).thenReturn(all)
        val got = useCase.invoke(page = 1, pageSize = 2)
        assertEquals(4, got.size)
    }

    @Test
    fun `invoke returns empty when repository returns empty`() = kotlinx.coroutines.test.runTest(dispatcher) {
        whenever(repo.getHoldings(org.mockito.kotlin.any(), org.mockito.kotlin.any())).thenReturn(emptyList())
        val got = useCase.invoke(page = 1, pageSize = 10)
        assertEquals(0, got.size)
    }
}


