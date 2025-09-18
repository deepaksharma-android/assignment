package com.example.assignment.data.repository

import com.example.assignment.data.remote.api.HoldingsApi
import com.example.assignment.domain.model.Holding
import com.example.assignment.domain.repository.HoldingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.PrimitiveIterator
import javax.inject.Inject

class HoldingsRepositoryImpl @Inject constructor(
    private val api: HoldingsApi,
    private val ioDispatcher: CoroutineDispatcher,
) : HoldingsRepository {
    override suspend fun getHoldings(page: Int, pageSize: Int): List<Holding> =
        withContext(ioDispatcher) {
            api.getHoldings().data?.userHolding?.map { it.toDomain() } ?: emptyList()
        }

}


