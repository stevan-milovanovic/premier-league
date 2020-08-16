package com.smobile.premierleague.repository

import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.api.StandingsNetworkResponse
import com.smobile.premierleague.db.StandingDao
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Standing instances
 */
@Singleton
@OpenForTesting
class StandingsRepository @Inject constructor(
    private val standingDao: StandingDao,
    private val leagueService: LeagueService
) {

    suspend fun loadStandings(leagueId: Int) = object : NetworkBoundResourceCoroutines
    <List<Standing>, StandingsNetworkResponse>() {
        override suspend fun createCall() = leagueService.getStandings(leagueId)

        override fun shouldFetch(databaseResult: List<Standing>?) =
            databaseResult == null || databaseResult.isEmpty()

        override suspend fun loadFromDb() = standingDao.getAll()

        override suspend fun saveCallResults(result: List<Standing>) {
            standingDao.insert(result)
        }

        override fun processResponse(response: StandingsNetworkResponse): List<Standing> =
            response.api.standings[0]
    }.build().asLiveData()

}