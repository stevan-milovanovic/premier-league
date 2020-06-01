package com.smobile.premierleague.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
    indices = [Index("teamId")],
    primaryKeys = ["id", "teamId"],
    foreignKeys = [ForeignKey(
        entity = Standing::class,
        parentColumns = ["id"],
        childColumns = ["teamId"],
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class Player(
    @field:SerializedName("player_id")
    val id: Int,
    @field:SerializedName("player_name")
    val name: String,
    val position: String?,
    val age: Int,
    val nationality: String,
    val league: String?,
    @field:Embedded(prefix = "shots_")
    val shots: Shots?,
    @field:Embedded(prefix = "goals_")
    val goals: Goals?,
    @field:Embedded(prefix = "passes_")
    val passes: Passes?,
    @field:Embedded(prefix = "tackles_")
    val tackles: Tackles?,
    @field:Embedded(prefix = "duels_")
    val duels: Duels?,
    @field:Embedded(prefix = "dribbles_")
    val dribbles: Dribbles?,
    @field:Embedded(prefix = "fouls_")
    val fouls: Fouls?
) {
    // does not show up in the response but set in post processing.
    var teamId: Int = -1

    data class Shots(
        val total: Int,
        val on: Int
    )

    data class Goals(
        val total: Int,
        val conceded: Int,
        val assists: Int
    )

    data class Passes(
        val total: Int,
        val key: Int,
        val accuracy: Int
    )

    data class Tackles(
        val total: Int,
        val blocks: Int,
        val interceptions: Int
    )

    data class Duels(
        val total: Int,
        val won: Int
    )

    data class Dribbles(
        val attempts: Int,
        val success: Int
    )

    data class Fouls(
        val drawn: Int,
        val committed: Int
    )
}