package com.smobile.premierleague.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.PlayerPosition

/**
 * Interface for database access for Player related operations
 */
@Dao
abstract class PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(player: List<Player>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(player: Player)

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, teamId, name, age, position, nationality FROM player WHERE id = :playerId")
    abstract fun getById(playerId: Int): LiveData<Player>

    @Query("SELECT * FROM player WHERE id = :playerOneId AND teamId = :teamId OR id = :playerTwoId AND teamId = :teamId")
    abstract fun getHeadToHeadDetails(
        teamId: Int,
        playerOneId: Int,
        playerTwoId: Int
    ): LiveData<List<Player>>

    fun loadOrdered(teamId: Int): LiveData<List<Player>> = getForTeam(teamId).map { players ->
        players.sortedWith(Comparator { player1, player2 ->
            val pos1 = PlayerPosition.mapToOrder(player1.position)
            val pos2 = PlayerPosition.mapToOrder(player2.position)
            return@Comparator if (pos1 == pos2) player1.id - player2.id else pos1 - pos2
        })
    }

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, teamId, name, age, position, nationality, imageUrl FROM player WHERE teamId = :teamId")
    abstract fun getForTeam(teamId: Int): LiveData<List<Player>>

}
