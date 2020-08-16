package com.smobile.premierleague.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.PlayerPosition

/**
 * Interface for database access for Player related operations
 */
@Dao
abstract class PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(player: List<Player>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(player: Player)

    @Query("SELECT id, teamId, name, age, position, nationality FROM player WHERE id = :playerId")
    abstract suspend fun getById(playerId: Int): Player

    @Query("SELECT * FROM player WHERE id = :playerOneId AND teamId = :teamId OR id = :playerTwoId AND teamId = :teamId")
    abstract suspend fun getHeadToHeadDetails(teamId: Int, playerOneId: Int, playerTwoId: Int): List<Player>

    suspend fun loadOrdered(teamId: Int) = getForTeam(teamId).sortedWith { player1, player2 ->
        val pos1 = PlayerPosition.mapToOrder(player1.position)
        val pos2 = PlayerPosition.mapToOrder(player2.position)

        if (pos1 == pos2) player1.id - player2.id else pos1 - pos2
    }

    @Query("SELECT id, teamId, name, age, position, nationality, imageUrl FROM player WHERE teamId = :teamId")
    protected abstract suspend fun getForTeam(teamId: Int): List<Player>

}