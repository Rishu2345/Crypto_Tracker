package com.buildsol.cryptotracker.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Any ViewModel test needs this. viewModelScope launches coroutines on
 * Dispatchers.Main, which doesn't exist in a plain JVM unit test (there's no
 * real Android main thread). This rule swaps Dispatchers.Main for a
 * StandardTestDispatcher before each test and restores it after, so
 * viewModelScope.launch { } actually runs on a dispatcher your test controls
 * (advanceTimeBy, advanceUntilIdle, etc.) instead of crashing.
 *
 * Usage in a test class:
 *
 *   @get:Rule
 *   val mainDispatcherRule = MainDispatcherRule()
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}