package com.smobile.premierleague.ui.team

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smobile.premierleague.Const
import com.smobile.premierleague.repository.PlayerRepository
import com.smobile.premierleague.testing.mock
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

/**
 * Unit class for [TeamViewModel]
 */
class TeamViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: PlayerRepository = mock()
    private val viewModel = TeamViewModel(repository)

    @Test
    fun testInitialState() {
        assertNotNull(viewModel.playerOne)
        assertNull(viewModel.playerOne.value)
        assertNotNull(viewModel.playerTwo)
        assertNull(viewModel.playerTwo.value)
    }

    @Test
    fun testLoadPlayers() {
        viewModel.loadPlayers(1)
        verify(repository).loadTeam(1, Const.SEASON)
    }

}