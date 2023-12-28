package com.smobile.premierleague.ui.headtohead

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smobile.premierleague.Const
import com.smobile.premierleague.repository.PlayerRepository
import com.smobile.premierleague.util.mock
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

/**
 * Unit test class for [HeadToHeadViewModel]
 */
class HeadToHeadViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository: PlayerRepository = mock()
    private val viewModel = HeadToHeadViewModel(repository)

    @Test
    fun testInitialState() {
        assertNotNull(viewModel.players)
        assertNull(viewModel.players.value)

        assertNotNull(viewModel.winnerId)
        assertNull(viewModel.winnerId.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun comparePlayerWithHimself() {
        viewModel.setParams(1, 1, 10)
    }

    @Test
    fun doNotFetchWithoutObservers() {
        viewModel.setParams(1, 2, 10)
        verify(repository, never()).loadHeadToHeadStatistics(1, 2, 10, Const.SEASON)
    }

    @Test
    fun fetchPlayersWhenWinnerIdObserved() {
        viewModel.setParams(1, 2, 10)
        viewModel.winnerId.observeForever(mock())
        verify(repository).loadHeadToHeadStatistics(1, 2, 10, Const.SEASON)
    }

}
