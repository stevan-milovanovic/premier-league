package com.smobile.premierleague.db

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.PlayerPosition
import java.util.*

/**
 * Interface for database access for Player related operations
 */
@Dao
abstract class PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(player: List<Player>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(player: Player)

    @Query("SELECT * FROM player WHERE id = :id")
    abstract fun getById(id: Int): LiveData<Player>

    fun loadOrdered(teamId: Int): LiveData<List<Player>> {
        return Transformations.map(getForTeam(teamId)) { players ->
            val order = SparseIntArray()
            players.withIndex().forEach {
                order.put(it.value.id, PlayerPosition.mapToOrder(it.value.position))
            }
            Collections.sort(players) { player1, player2 ->
                val pos1 = order.get(player1.id)
                val pos2 = order.get(player2.id)
                pos1 - pos2
            }
            players
        }
    }

    @Query("SELECT * FROM player WHERE teamId = :teamId")
    protected abstract fun getForTeam(teamId: Int): LiveData<List<Player>>

}