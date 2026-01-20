package com.darioossa.myspa.core.ui

import com.darioossa.myspa.core.Reducer
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseViewModelTest {
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    private val state = object : Reducer.ViewState {}

    private lateinit var viewModel: BaseMVIViewModel<Reducer.ViewState, Reducer.ViewEvent, Reducer.ViewEffect>

    private val reducer = mock<Reducer<Reducer.ViewState, Reducer.ViewEvent, Reducer.ViewEffect>>(
        mode = MockMode.autofill
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = BaseMVIViewModel(state, reducer)
    }

    @Test
    fun `uses the state provided`() {
        assertIs<Reducer.ViewState>(viewModel.state.value)
        assertSame(state, viewModel.state.value)
    }

    @Test
    fun `sends effects when received`() = runTest {
        val effect = object: Reducer.ViewEffect {}
        viewModel.sendEffect(effect)
        assertSame(effect, viewModel.effect.first())
    }

    @Test
    fun `uses the reducer and state provided on events`() = runTest {
        val expectedState = object : Reducer.ViewState { val value = "" }
        val event = object: Reducer.ViewEvent {}
        testSendEventResult(event, expectedState, null)
    }

    private suspend fun testSendEventResult(
        event: Reducer.ViewEvent,
        expectedState: Reducer.ViewState,
        effect: Reducer.ViewEffect? = null
    ) {
        val prevState = viewModel.state.value
        every { reducer.reduce(any(), event) } returns Pair(expectedState, effect)
        viewModel.sendEvent(event)
        verify { reducer.reduce(prevState, event) }
        assertSame(expectedState, viewModel.state.value)
        effect?.let {
            assertSame(effect, viewModel.effect.first())
        }
    }

    @Test
    fun `uses the reducer state and effect provided on events related to effects`() =
        runTest {
            val event = object: Reducer.ViewEvent {}
            val expectedState = object : Reducer.ViewState { val value = "" }
            val effect = object : Reducer.ViewEffect {}
            testSendEventResult(event, expectedState, effect)
        }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}