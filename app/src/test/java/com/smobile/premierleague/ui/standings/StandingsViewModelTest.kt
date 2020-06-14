package com.smobile.premierleague.ui.standings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.StandingsRepository
import com.smobile.premierleague.testing.mock
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*

/**
 * Unit test class for [StandingsViewModel]
 */
class StandingsViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository: StandingsRepository = mock()
    private val viewModel = StandingsViewModel(repository)

    @Test
    fun testInitialState() {
        assertNotNull(viewModel.standings)
        assertNull(viewModel.standings.value)
        assertFalse(viewModel.standings.hasObservers())
    }

    @Test
    fun doNotFetchWithoutObservers() {
        viewModel.setLeagueId(100)
        verify(repository, never()).loadStandings(anyInt())
    }

    @Test
    fun fetchWhenObserved() {
        viewModel.setLeagueId(100)
        viewModel.standings.observeForever(mock())
        verify(repository).loadStandings(100)
    }

    @Test
    fun changeWhileObserved() {
        viewModel.standings.observeForever(mock())

        viewModel.setLeagueId(100)
        viewModel.setLeagueId(200)

        verify(repository).loadStandings(100)
        verify(repository).loadStandings(200)
    }

    @Test
    fun testLoadStandings() {
        viewModel.standings.observeForever(mock())
        viewModel.setLeagueId(1)
        verify(repository).loadStandings(1)
        reset(repository)
        viewModel.setLeagueId(2)
        verify(repository).loadStandings(2)
    }

    @Test
    fun testSendResultToUI() {
        val leagueOne = MutableLiveData<Resource<List<Standing>>>()
        val leagueTwo = MutableLiveData<Resource<List<Standing>>>()

        `when`(repository.loadStandings(1)).thenReturn(leagueOne)
        `when`(repository.loadStandings(2)).thenReturn(leagueTwo)

        val observer = mock<Observer<Resource<List<Standing>>>>()
        viewModel.standings.observeForever(observer)
        viewModel.setLeagueId(1)
        verify(observer, never()).onChanged(any())
        val leagueOneValue = Resource.success(emptyList<Standing>())

        leagueOne.value = leagueOneValue
        verify(observer).onChanged(leagueOneValue)

        reset(observer)

        val leagueTwoValue = Resource.success(emptyList<Standing>())
        leagueTwo.value = leagueTwoValue
        verify(observer, never()).onChanged(any())
        viewModel.setLeagueId(2)
        verify(observer).onChanged(leagueTwoValue)
        verifyNoMoreInteractions(observer)
    }

}