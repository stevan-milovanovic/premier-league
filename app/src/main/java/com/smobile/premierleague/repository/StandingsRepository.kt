package com.smobile.premierleague.repository

import androidx.lifecycle.LiveData
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.api.StandingsNetworkResponse
import com.smobile.premierleague.db.StandingDao
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.model.base.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Standing instances
 */
@Singleton
class StandingsRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val standingDao: StandingDao,
    private val leagueService: LeagueService
) {

    fun loadStandings(leagueId: Int): LiveData<Resource<List<Standing>>> {
        return object :
            NetworkBoundResource<List<Standing>, StandingsNetworkResponse>(appExecutors) {
            override fun saveCallResult(item: StandingsNetworkResponse) {
                standingDao.insert(item.api.standings[0])
            }

            override fun shouldFetch(data: List<Standing>?) = data.isNullOrEmpty()

            override fun loadFromDb() = standingDao.getAll()

            override fun createCall() = leagueService.getStandings(leagueId)
        }.asLiveData()
    }

}