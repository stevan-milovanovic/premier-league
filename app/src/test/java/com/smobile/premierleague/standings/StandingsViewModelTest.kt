package com.smobile.premierleague.standings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.StandingsRepository
import com.smobile.premierleague.testing.mock
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class StandingsViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(StandingsRepository::class.java)
    private val viewModel = StandingsViewModel(repository)

    @Test
    fun testInitialState() {
        assertThat(viewModel.standings, notNullValue())
        verify(repository, never()).loadStandings(anyInt())
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
        viewModel.setLeagueId(2)
        verify(observer).onChanged(leagueTwoValue)
    }

}