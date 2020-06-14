package com.smobile.premierleague.ui.team

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smobile.premierleague.Const
import com.smobile.premierleague.repository.PlayerRepository
import com.smobile.premierleague.testing.mock
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*


class TeamViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(PlayerRepository::class.java)
    private val viewModel = TeamViewModel(repository)

    @Test
    fun testInitialState() {
        assertNotNull(viewModel.players)
        assertNotNull(viewModel.playerOne)
        assertNotNull(viewModel.playerTwo)
    }

    @Test
    fun doNotFetchWithoutObservers() {
        viewModel.setTeamId(1)
        verify(repository, never()).loadTeam(1, Const.SEASON)
    }

    @Test
    fun testSetTeamId() {
        viewModel.setTeamId(1)
        viewModel.players.observeForever(mock())
        verify(repository).loadTeam(1, Const.SEASON)
    }

}