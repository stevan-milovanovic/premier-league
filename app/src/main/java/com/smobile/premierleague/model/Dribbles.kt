package com.smobile.premierleague.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Dribbles(
    val attempts: Int,
    val success: Int
)