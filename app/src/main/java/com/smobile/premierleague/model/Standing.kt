package com.smobile.premierleague.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(
    indices = [Index("id")]
)
data class Standing(
    @PrimaryKey
    val id: Int,
    @field:Json(name = "idx")
    val rank: Int,
    @field:Json(name = "shortName")
    val name: String,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    @field:Json(name = "pts")
    val points: Int
) {
    // does not show up in the response but set in post processing.
    var logo: String? = null
}