package com.smobile.premierleague.repository

import androidx.lifecycle.LiveData
import com.smobile.premierleague.Const
import com.smobile.premierleague.Const.LEAGUE
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.api.TeamNetworkResponse
import com.smobile.premierleague.db.PlayerDao
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Player instances
 */
@Singleton
@OpenForTesting
class PlayerRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val leagueService: LeagueService
) {
    suspend fun loadTeam(teamId: Int, season: String): LiveData<Resource<List<Player>>> {
        return object : NetworkBoundResourceCoroutines<List<Player>, TeamNetworkResponse>() {
            override suspend fun createCall() = leagueService.getTeam(teamId, season)

            override fun shouldFetch(databaseResult: List<Player>?) =
                databaseResult == null || databaseResult.isEmpty()

            override suspend fun loadFromDb() = playerDao.loadOrdered(teamId)

            override suspend fun saveCallResults(result: List<Player>) {
                result.forEach {
                    it.teamId = teamId
                    it.imageUrl = Const.API_PLAYER_URL.replace("{id}", it.id.toString())
                }
                playerDao.insert(result)
            }

            override fun processResponse(response: TeamNetworkResponse) = response.api.players

        }.build().asLiveData()
    }

    suspend fun loadHeadToHeadStatistics(
        playerOneId: Int,
        playerTwoId: Int,
        teamId: Int,
        season: String
    ): LiveData<Resource<List<Player>>> {
        return object : NetworkBoundResourceCoroutines<List<Player>, TeamNetworkResponse>() {
            override suspend fun createCall() = leagueService.getTeamStatistics(teamId, season)

            override fun shouldFetch(databaseResult: List<Player>?): Boolean {
                if (databaseResult == null || databaseResult.isEmpty()) {
                    return true
                }
                return databaseResult.any {
                    it.shots == null &&
                            it.goals == null &&
                            it.passes == null &&
                            it.tackles == null &&
                            it.duels == null &&
                            it.dribbles == null &&
                            it.fouls == null
                }
            }

            override suspend fun loadFromDb() =
                playerDao.getHeadToHeadDetails(teamId, playerOneId, playerTwoId)

            override suspend fun saveCallResults(result: List<Player>) {
                result.forEach {
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

            override fun processResponse(response: TeamNetworkResponse) = response.api.players
        }.build().asLiveData()
    }

}