package com.smobile.premierleague.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smobile.premierleague.model.Player

/**
 * Interface for database access for Player related operations
 */
@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(player: List<Player>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(player: Player)

    @Query(
        """
            SELECT * FROM player
            WHERE teamId = :teamId
            ORDER BY position ASC"""
    )
    fun getForTeam(teamId: Int): LiveData<List<Player>>

    @Query("SELECT * FROM player WHERE id = :id")
    fun getById(id: String): LiveData<Player>

}