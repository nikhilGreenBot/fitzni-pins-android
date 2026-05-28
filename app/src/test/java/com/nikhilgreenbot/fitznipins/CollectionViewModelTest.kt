package com.nikhilgreenbot.fitznipins.presentation.collection

import app.cash.turbine.test
import com.nikhilgreenbot.fitznipins.domain.model.FitzNiResult
import com.nikhilgreenbot.fitznipins.domain.model.UserFacingError
import com.nikhilgreenbot.fitznipins.domain.model.UserPin
import com.nikhilgreenbot.fitznipins.domain.usecase.DeletePinUseCase
import com.nikhilgreenbot.fitznipins.domain.usecase.ObserveCollectionUseCase
import com.nikhilgreenbot.fitznipins.domain.usecase.SearchCollectionUseCase
import com.nikhilgreenbot.fitznipins.domain.usecase.UpsertPinUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val observeCollection: ObserveCollectionUseCase = mockk()
    private val searchCollection:  SearchCollectionUseCase  = mockk()
    private val deletePin:         DeletePinUseCase          = mockk()
    private val upsertPin:         UpsertPinUseCase          = mockk()

    private lateinit var viewModel: CollectionViewModel

    private val fakePin = UserPin(
        id = "test-1",
        title = "Mickey Mouse Icon Pin",
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { observeCollection() } returns flowOf(listOf(fakePin))
        every { searchCollection(any()) } returns flowOf(emptyList())
        viewModel = CollectionViewModel(observeCollection, searchCollection, deletePin, upsertPin)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state shows collection from repository`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(listOf(fakePin), state.pins)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search query change updates state`() = runTest {
        viewModel.onEvent(CollectionEvent.SearchQueryChanged("mickey"))
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("mickey", state.searchQuery)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `delete pin success emits snackbar effect`() = runTest {
        coEvery { deletePin("test-1") } returns FitzNiResult.Success(Unit)

        viewModel.effects.test {
            viewModel.onEvent(CollectionEvent.DeletePin("test-1"))
            testDispatcher.scheduler.advanceUntilIdle()
            val effect = awaitItem()
            assertTrue(effect is CollectionEffect.ShowSnackbar)
            assertEquals("Pin removed", (effect as CollectionEffect.ShowSnackbar).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `delete pin failure sets error in state`() = runTest {
        coEvery { deletePin("test-1") } returns FitzNiResult.Error(UserFacingError.Unknown)

        viewModel.onEvent(CollectionEvent.DeletePin("test-1"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(UserFacingError.Unknown, state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `dismiss error clears error state`() = runTest {
        coEvery { deletePin(any()) } returns FitzNiResult.Error(UserFacingError.Unknown)
        viewModel.onEvent(CollectionEvent.DeletePin("test-1"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(CollectionEvent.DismissError)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
