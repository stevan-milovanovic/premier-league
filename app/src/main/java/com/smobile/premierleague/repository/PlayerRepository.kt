package com.smobile.premierleague.repository

import com.smobile.premierleague.Const
import com.smobile.premierleague.Const.LEAGUE
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.api.TeamNetworkResponse
import com.smobile.premierleague.db.PlayerDao
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
    private val networkService: LeagueService
) {

    fun loadTeam(teamId: Int, season: String) = fetchData(
        databaseQuery = { playerDao.loadOrdered(teamId) },
        networkCall = { networkService.getTeam(teamId, season) },
        saveCallResult = { result: TeamNetworkResponse ->
            result.api.players.forEach {
                it.teamId = teamId
                it.imageUrl = Const.API_PLAYER_URL.replace("{id}", it.id.toString())
            }
            playerDao.insert(result.api.players)
        }
    )

    fun loadHeadToHeadStatistics(
        playerOneId: Int,
        playerTwoId: Int,
        teamId: Int,
        season: String
    ) = fetchData(
        databaseQuery = { playerDao.getHeadToHeadDetails(teamId, playerOneId, playerTwoId) },
        networkCall = { networkService.getTeamStatistics(teamId, season) },
        saveCallResult = { result ->
            result.api.players.forEach {
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
    )

}