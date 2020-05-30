package com.smobile.premierleague.api

import com.google.gson.annotations.SerializedName
import com.smobile.premierleague.model.Standing

data class StandingsNetworkResponse(
    @SerializedName("api")
    val api: StandingsNetworkApiResponse
)

data class StandingsNetworkApiResponse(
    val results: Int,
    val standings: List<List<Standing>>
)