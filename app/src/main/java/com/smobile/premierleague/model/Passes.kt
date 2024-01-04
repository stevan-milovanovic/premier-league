package com.smobile.premierleague.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Passes(
    val total: Int,
    val key: Int,
    val accuracy: Int
)