package com.smobile.premierleague.repository

import androidx.lifecycle.LiveData
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.api.TeamNetworkResponse
import com.smobile.premierleague.db.LeagueDb
import com.smobile.premierleague.db.PlayerDao
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Player instances
 */
@Singleton
class PlayerRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: LeagueDb,
    private val playerDao: PlayerDao,
    private val leagueService: LeagueService
) {
    fun loadTeam(teamId: Int, season: String): LiveData<Resource<List<Player>>> {
        return object : NetworkBoundResource<List<Player>, TeamNetworkResponse>(appExecutors) {
            override fun saveCallResult(item: TeamNetworkResponse) {
                item.api.players.forEach {
                    it.teamId = teamId
                }
                playerDao.insert(item.api.players)
            }

            override fun shouldFetch(data: List<Player>?) = data == null || data.isEmpty()

            override fun loadFromDb() = playerDao.loadOrdered(teamId)

            override fun createCall() = leagueService.getTeam(teamId, season)
        }.asLiveData()
    }

    fun loadForId(playerId: Int): LiveData<Player> {
        return playerDao.getById(playerId)
    }

}