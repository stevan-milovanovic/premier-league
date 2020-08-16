package com.smobile.premierleague.ui.headtohead

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.Const.SEASON
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for [HeadToHeadFragment]
 */
class HeadToHeadViewModel @Inject constructor(private val playerRepository: PlayerRepository) :
    ViewModel() {

    val winnerId = MutableLiveData<Int>()

    suspend fun getPlayers(teamId: Int, playerOneId: Int, playerTwoId: Int) =
        withContext(Dispatchers.IO) {
            playerRepository.loadHeadToHeadStatistics(
                playerOneId,
                playerTwoId,
                teamId,
                SEASON
            )
        }

    fun determineWinnerId(players: List<Player>) {
        winnerId.value = comparePlayers(players)
    }

    private fun comparePlayers(players: List<Player>): Int {
        if (players.size != 2) return 0
        players[0].goals?.let { playerOneGoals ->
            players[1].goals?.let { playerTwoGoals ->
                if (playerOneGoals.total > playerTwoGoals.total) return players[0].id
                if (playerOneGoals.total < playerTwoGoals.total) return players[1].id

                if (playerOneGoals.assists > playerTwoGoals.assists) return players[0].id
                if (playerOneGoals.assists < playerTwoGoals.assists) return players[1].id

                players[0].passes?.let { playerOnePasses ->
                    players[1].passes?.let { playerTwoPasses ->
                        if (playerOnePasses.total > playerTwoPasses.total) return players[0].id
                        if (playerOnePasses.total < playerTwoPasses.total) return players[1].id
                    }
                }
            }
        }
        return 0
    }

}