package com.smobile.premierleague.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smobile.premierleague.model.Standing

/**
 * Interface for database access for Standing related operations
 */
@Dao
interface StandingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(standings: List<Standing>)

    @Query("SELECT * FROM standing ORDER BY rank ASC")
    suspend fun getAll(): List<Standing>

    @Query("SELECT * FROM standing WHERE id = :id")
    suspend fun getById(id: String): Standing

}