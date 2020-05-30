package com.smobile.premierleague.db

import androidx.lifecycle.LiveData
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
    fun insert(standings: List<Standing>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(standing: Standing)

    @Query("SELECT * FROM standing ORDER BY rank ASC")
    fun getAll(): LiveData<List<Standing>>

    @Query("SELECT * FROM standing WHERE id = :id")
    fun getById(id: String): LiveData<Standing>

}