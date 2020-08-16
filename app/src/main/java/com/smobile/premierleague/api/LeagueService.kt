package com.smobile.premierleague.api

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface LeagueService {

    @GET("leagueTable/{leagueId}")
    suspend fun getStandings(
        @Path("leagueId") leagueId: Int
    ): StandingsNetworkResponse

    @GET("players/squad/{teamId}/{season}")
    suspend fun getTeam(
        @Path("teamId") teamId: Int,
        @Path("season") season: String
    ): TeamNetworkResponse

    @GET("players/team/{teamId}/{season}")
    suspend fun getTeamStatistics(
        @Path("teamId") teamId: Int,
        @Path("season") season: String
    ): TeamNetworkResponse

}