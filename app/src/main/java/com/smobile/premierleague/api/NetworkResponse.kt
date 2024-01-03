package com.smobile.premierleague.api

import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.Standing
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StandingsNetworkResponse(
    @Json(name = "api")
    val api: StandingsNetworkApiResponse
)

@JsonClass(generateAdapter = true)
data class StandingsNetworkApiResponse(
    val results: Int,
    val standings: List<List<Standing>>
)

@JsonClass(generateAdapter = true)
data class TeamNetworkResponse(
    @Json(name = "api")
    val api: TeamNetworkApiResponse
)

@JsonClass(generateAdapter = true)
data class TeamNetworkApiResponse(
    val results: Int,
    val players: List<Player>
)