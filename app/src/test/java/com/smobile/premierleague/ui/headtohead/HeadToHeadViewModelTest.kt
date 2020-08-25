package com.smobile.premierleague.ui.headtohead

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.smobile.premierleague.Const
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.repository.PlayerRepository
import com.smobile.premierleague.testing.mock
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
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
        assertNotNull(viewModel.winnerId)
        assertNull(viewModel.winnerId.value)
    }

    @Test
    fun testGetPlayers() {
        viewModel.getPlayers(1, 200, 300)
        verify(repository).loadHeadToHeadStatistics(200, 300, 1, Const.SEASON)
    }

    @Test
    fun testDetermineWinnerIdIfThereAreNoPlayers() {
        val observer = mock<Observer<Int>>()
        viewModel.winnerId.observeForever(observer)
        viewModel.determineWinnerId(listOf())
        verify(observer).onChanged(0)
    }

    @Test
    fun testDetermineWinnerIdWhenPlayersDoNotHaveGoals() {
        val playerOne: Player = mock()
        val playerTwo: Player = mock()
        val observer = mock<Observer<Int>>()
        viewModel.winnerId.observeForever(observer)
        viewModel.determineWinnerId(listOf(playerOne, playerTwo))
        verify(observer).onChanged(0)
    }

    @Test
    fun testDetermineWinnerIdWhenFirstPlayerHasMoreGoals() {
        val playerOne: Player = mock()
        `when`(playerOne.id).thenReturn(1)
        val goals: Player.Goals = mock()
        `when`(goals.total).thenReturn(1)
        `when`(playerOne.goals).thenReturn(goals)
        val playerTwo: Player = mock()
        `when`(playerTwo.goals).thenReturn(mock())
        val observer = mock<Observer<Int>>()
        viewModel.winnerId.observeForever(observer)
        viewModel.determineWinnerId(listOf(playerOne, playerTwo))
        verify(observer).onChanged(1)
    }

    @Test
    fun testDetermineWinnerIdWhenSecondPlayerHasMoreGoals() {
        val playerOne: Player = mock()
        `when`(playerOne.goals).thenReturn(mock())
        val playerTwo: Player = mock()
        `when`(playerTwo.id).thenReturn(2)
        val goals: Player.Goals = mock()
        `when`(goals.total).thenReturn(1)
        `when`(playerTwo.goals).thenReturn(goals)
        val observer = mock<Observer<Int>>()
        viewModel.winnerId.observeForever(observer)
        viewModel.determineWinnerId(listOf(playerOne, playerTwo))
        verify(observer).onChanged(2)
    }

}