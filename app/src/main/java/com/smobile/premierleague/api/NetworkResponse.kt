package com.smobile.premierleague.api

import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.Standing
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StandingsNetworkResponse(
    val status: String,
    val response: StandingsNetworkApiResponse
)

@JsonClass(generateAdapter = true)
data class StandingsNetworkApiResponse(
    val standing: List<Standing>
)

@JsonClass(generateAdapter = true)
data class TeamNetworkResponse(
    val status: String,
    val response: TeamNetworkApiResponse
)

@JsonClass(generateAdapter = true)
data class TeamNetworkApiResponse(
    val list: TeamNetworkApiListResponse
)

@JsonClass(generateAdapter = true)
data class TeamNetworkApiListResponse(
    val squad: List<TeamNetworkApiMembersResponse>
)

@JsonClass(generateAdapter = true)
data class TeamNetworkApiMembersResponse(
    val title: String,
    val members: List<Player>
)