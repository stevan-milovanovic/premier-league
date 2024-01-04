package com.smobile.premierleague.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Fouls(
    val drawn: Int,
    val committed: Int
)