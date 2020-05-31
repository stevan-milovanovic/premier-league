package com.smobile.premierleague.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    indices = [Index("id")]
)
data class Standing(
    @PrimaryKey
    @field:SerializedName("team_id")
    val id: Int,
    val rank: Int,
    @field:SerializedName("teamName")
    val name: String,
    val logo: String,
    @field:SerializedName("all")
    @field:Embedded(prefix = "statistics_")
    val statistics: TeamStatistic,
    val points: Int
) {
    data class TeamStatistic(
        @field:SerializedName("matchsPlayed")
        val played: Int,
        val win: Int,
        val draw: Int,
        val lose: Int
    )
}