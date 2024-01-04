package com.smobile.premierleague.util

import com.smobile.premierleague.api.StandingsNetworkApiResponse
import com.smobile.premierleague.api.StandingsNetworkResponse
import com.smobile.premierleague.api.TeamNetworkApiResponse
import com.smobile.premierleague.api.TeamNetworkResponse
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.model.TeamStatistic
import java.util.*

object TestUtil {

    fun createTeamNetworkResponse(teamId: Int) = TeamNetworkResponse(
        TeamNetworkApiResponse(10, createPlayers(teamId))
    )

    fun createStandingsNetworkResponse() = StandingsNetworkResponse(
        StandingsNetworkApiResponse(10, Collections.singletonList(createStandings()))
    )

    private fun createStandings(): List<Standing> {
        return (0 until 10).map {
            createStanding(it)
        }
    }

    private fun createStanding(index: Int) = Standing(
        index,
        index,
        index.toString(),
        index.toString(),
        TeamStatistic(index, index, index, index),
        index
    )

    private fun createPlayers(teamId: Int): List<Player> {
        return (0 until 10).map {
            createPlayer(it, teamId)
        }
    }

    private fun createPlayer(index: Int, teamId: Int) = Player(
        index, index.toString(), null, index, index.toString(),
        null, null, null, null, null, null, null, null
    ).apply { this.teamId = teamId }

}