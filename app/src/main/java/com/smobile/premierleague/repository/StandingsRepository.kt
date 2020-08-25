package com.smobile.premierleague.repository

import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.db.StandingDao
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
    private val networkService: LeagueService
) {

    fun loadStandings(leagueId: Int) = fetchData(
        databaseQuery = { standingDao.getAll() },
        networkCall = { networkService.getStandings(leagueId) },
        saveCallResult = { standingDao.insert(it.api.standings[0]) }
    )

}