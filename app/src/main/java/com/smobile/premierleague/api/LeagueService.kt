package com.smobile.premierleague.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points
 */
interface LeagueService {

    @GET("football-get-standing-all")
    fun getStandings(
        @Query("leagueid") leagueId: Int
    ): LiveData<ApiResponse<StandingsNetworkResponse>>

    @GET("football-get-list-player")
    fun getTeam(
        @Query("teamid") teamId: Int,
    ): LiveData<ApiResponse<TeamNetworkResponse>>

}