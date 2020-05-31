package com.smobile.premierleague.model

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
    val nationality: String
) {
    // does not show up in the response but set in post processing.
    var teamId: Int = -1
}