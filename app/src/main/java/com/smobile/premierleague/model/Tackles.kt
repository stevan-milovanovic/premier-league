package com.smobile.premierleague.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tackles(
    val total: Int,
    val blocks: Int,
    val interceptions: Int
)