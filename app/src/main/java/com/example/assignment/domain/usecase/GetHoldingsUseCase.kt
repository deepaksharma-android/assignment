package com.example.assignment.domain.usecase

import com.example.assignment.domain.model.Holding
import com.example.assignment.domain.repository.HoldingsRepository
import javax.inject.Inject

interface GetHoldingsUseCase {
    suspend operator fun invoke(page: Int, pageSize: Int): List<Holding>
}

class GetHoldingsUseCaseImpl @Inject constructor(
    private val repository: HoldingsRepository
) : GetHoldingsUseCase {
    override suspend fun invoke(page: Int, pageSize: Int): List<Holding> {
        return repository.getHoldings(page, pageSize)?: emptyList()
    }

}


