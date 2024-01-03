package com.smobile.premierleague.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Shots(
    val total: Int,
    val on: Int
)