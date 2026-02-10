package com.smobile.premierleague.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
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
    val id: Int,
    val name: String,
    val positionId: Int = PlayerPosition.UNKNOWN.order,
    @field:Json(name = "positionIdsDesc")
    val position: String?,
    val age: Int,
    @field:Json(name = "cname")
    val nationality: String?,
    val goals: Int?,
    val penalties: Int?,
    val assists: Int?,
) {
    // does not show up in the response but set in post processing.
    var teamId: Int = -1
    var squad: String? = null
    var imageUrl: String? = null
}