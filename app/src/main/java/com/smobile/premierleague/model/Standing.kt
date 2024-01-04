package com.smobile.premierleague.model

import androidx.room.Embedded
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
    @field:Json(name = "team_id")
    val id: Int,
    val rank: Int,
    @field:Json(name = "teamName")
    val name: String,
    val logo: String,
    @field:Json(name = "all")
    @field:Embedded(prefix = "statistics_")
    val statistics: TeamStatistic,
    val points: Int
)