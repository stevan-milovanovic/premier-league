package com.smobile.premierleague.repository

import androidx.lifecycle.LiveData
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.Const
import com.smobile.premierleague.Const.LEAGUE
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.api.TeamNetworkResponse
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
    private val playerDao: PlayerDao,
    private val leagueService: LeagueService
) {
    fun loadTeam(teamId: Int, season: String): LiveData<Resource<List<Player>>> {
        return object : NetworkBoundResource<List<Player>, TeamNetworkResponse>(appExecutors) {
            override fun saveCallResult(item: TeamNetworkResponse) {
                item.api.players.forEach {
                    it.teamId = teamId
                    it.imageUrl = Const.API_PLAYER_URL.replace("{id}", it.id.toString())
                }
                playerDao.insert(item.api.players)
            }

            override fun shouldFetch(data: List<Player>?) = data.isNullOrEmpty()

            override fun loadFromDb() = playerDao.loadOrdered(teamId)

            override fun createCall() = leagueService.getTeam(teamId, season)
        }.asLiveData()
    }

    fun loadHeadToHeadStatistics(
        playerOneId: Int,
        playerTwoId: Int,
        teamId: Int,
        season: String
    ): LiveData<Resource<List<Player>>> {
        return object : NetworkBoundResource<List<Player>, TeamNetworkResponse>(appExecutors) {
            override fun saveCallResult(item: TeamNetworkResponse) {
                item.api.players.forEach {
                    if (it.id == playerOneId) {
                        if (it.league == LEAGUE) {
                            it.teamId = teamId
                            it.imageUrl = Const.API_PLAYER_URL.replace("{id}", it.id.toString())
                            playerDao.insert(it)
                        }
                    }
                    if (it.id == playerTwoId) {
                        if (it.league == LEAGUE) {
                            it.teamId = teamId
                            it.imageUrl = Const.API_PLAYER_URL.replace("{id}", it.id.toString())
                            playerDao.insert(it)
                        }
                    }
                }
            }

            override fun shouldFetch(data: List<Player>?): Boolean {
                if (data.isNullOrEmpty()) {
                    return true
                }
                return data.any {
                    it.shots == null && it.goals == null && it.passes == null && it.tackles == null &&
                            it.duels == null && it.dribbles == null && it.fouls == null
                }
            }

            override fun loadFromDb(): LiveData<List<Player>> {
                return playerDao.getHeadToHeadDetails(teamId, playerOneId, playerTwoId)
            }

            override fun createCall() = leagueService.getTeamStatistics(teamId, season)
        }.asLiveData()
    }

    fun loadForId(playerId: Int): LiveData<Player> {
        return playerDao.getById(playerId)
    }

}
