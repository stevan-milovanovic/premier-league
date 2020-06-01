package com.smobile.premierleague.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface LeagueService {

    @GET("leagueTable/{leagueId}")
    fun getStandings(@Path("leagueId") leagueId: Int): LiveData<ApiResponse<StandingsNetworkResponse>>

    @GET("players/squad/{teamId}/{season}")
    fun getTeam(
        @Path("teamId") teamId: Int,
        @Path("season") season: String
    ): LiveData<ApiResponse<TeamNetworkResponse>>

    @GET("players/team/{teamId}/{season}")
    fun getTeamStatistics(
        @Path("teamId") teamId: Int,
        @Path("season") season: String
    ): LiveData<ApiResponse<TeamNetworkResponse>>

}