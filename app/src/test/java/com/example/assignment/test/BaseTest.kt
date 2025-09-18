package com.example.assignment.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTest {
    protected val dispatcher = StandardTestDispatcher()

    @Before
    fun baseSetUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun baseTearDown() {
        Dispatchers.resetMain()
    }

    protected fun loadJson(name: String): String =
        checkNotNull(javaClass.classLoader).getResource(name)?.readText()
            ?: error("Missing test resource: $name")
}


