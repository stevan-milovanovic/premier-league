package com.smobile.premierleague.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Goals(
    val total: Int,
    val conceded: Int,
    val assists: Int
)