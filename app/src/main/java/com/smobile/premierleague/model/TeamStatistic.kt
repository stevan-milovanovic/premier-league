package com.smobile.premierleague.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamStatistic(
    @field:Json(name = "matchsPlayed")
    val played: Int,
    val win: Int,
    val draw: Int,
    val lose: Int
)