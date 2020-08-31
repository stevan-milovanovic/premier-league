package com.smobile.premierleague.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.smobile.premierleague.api.ApiResponse
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.api.TeamNetworkResponse
import com.smobile.premierleague.db.LeagueDb
import com.smobile.premierleague.db.PlayerDao
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.util.mock
import com.smobile.premierleague.util.ApiUtil.successCall
import com.smobile.premierleague.util.InstantAppExecutors
import com.smobile.premierleague.util.TestUtil.createTeamNetworkResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

/**
 * Unit test class for [PlayerRepository]
 */
class PlayerRepositoryTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dao: PlayerDao = mock()
    private val service: LeagueService = mock()

    private lateinit var repository: PlayerRepository

    @Before
    fun init() {
        val db: LeagueDb = mock()
        `when`(db.playerDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = PlayerRepository(InstantAppExecutors(), dao, service)
    }

    @Test
    fun testLoadTeamFromNetwork() {
        val teamId = 10
        val season = "season"
        val dbData = MutableLiveData<List<Player>>()
        `when`(dao.loadOrdered(teamId)).thenReturn(dbData)
        val teamNetworkResource = createTeamNetworkResponse(teamId)
        val call = successCall(teamNetworkResource)
        `when`(service.getTeam(teamId, season)).thenReturn(call)

        val data = repository.loadTeam(10, season)
        verify(dao).loadOrdered(teamId)
        verifyNoMoreInteractions(service)

        val observer: Observer<Resource<List<Player>>> = mock()
        data.observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.loading(null))
        val updatedDbData = MutableLiveData<List<Player>>()
        `when`(dao.loadOrdered(teamId)).thenReturn(updatedDbData)

        dbData.postValue(emptyList())
        verify(service).getTeam(teamId, season)
        verify(dao).insert(teamNetworkResource.api.players)

        updatedDbData.postValue(teamNetworkResource.api.players)
        verify(observer).onChanged(Resource.success(teamNetworkResource.api.players))
    }

    @Test
    fun loadTeamFromNetworkError() {
        val teamId = 10
        val season = "season"
        val dbData = MutableLiveData<List<Player>>()
        `when`(dao.loadOrdered(teamId)).thenReturn(dbData)
        val apiResponse = MutableLiveData<ApiResponse<TeamNetworkResponse>>()
        `when`(service.getTeam(teamId, season)).thenReturn(apiResponse)

        dbData.postValue(emptyList())

        val observer: Observer<Resource<List<Player>>> = mock()
        repository.loadTeam(teamId, season).observeForever(observer)
        verify(observer).onChanged(Resource.loading(emptyList()))

        apiResponse.postValue(ApiResponse.create(Exception("example")))
        verify(observer).onChanged(Resource.error("example", emptyList()))
    }

}