package com.smobile.premierleague.ui.headtohead

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.PlayerRepository
import com.smobile.premierleague.util.AbsentLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for [HeadToHeadScreen]
 */
@HiltViewModel
class HeadToHeadViewModel @Inject constructor(
    playerRepository: PlayerRepository
) : ViewModel() {

    private val teamId: MutableLiveData<Int> = MutableLiveData()
    private val playerOneId: MutableLiveData<Int> = MutableLiveData()
    private val playerTwoId: MutableLiveData<Int> = MutableLiveData()

    val players: LiveData<Resource<List<Player>>> =
        teamId.switchMap { teamId ->
            playerOneId.value?.let { playerOneId ->
                playerTwoId.value?.let { playerTwoId ->
                    if (playerOneId != 0 && playerTwoId != 0) {
                        playerRepository.loadHeadToHeadStatistics(
                            playerOneId,
                            playerTwoId,
                            teamId
                        )
                    } else {
                        AbsentLiveData.create()
                    }
                }
            }
        }

    fun setParams(playerOneId: Int, playerTwoId: Int, teamId: Int) {
        if (this.playerOneId.value == playerOneId && this.playerTwoId.value == playerTwoId && this.teamId.value == teamId) {
            return
        }

        if (playerOneId == playerTwoId) {
            throw IllegalArgumentException("It is not possible to compare a player with himself.")
        }

        this.playerOneId.value = playerOneId
        this.playerTwoId.value = playerTwoId
        this.teamId.value = teamId
    }

    val winnerId: LiveData<Int> = players.switchMap { resource ->
        resource.data?.let { players ->
            comparePlayers(players)
        }
    }

    private fun comparePlayers(players: List<Player>): LiveData<Int> {
        if (players.size != 2) return AbsentLiveData.create()

        players[0].goals?.let { playerOneGoals ->
            players[1].goals?.let { playerTwoGoals ->
                if (playerOneGoals > playerTwoGoals) return playerOneId
                if (playerOneGoals < playerTwoGoals) return playerTwoId
            }
        }

        players[0].assists?.let { playerOneAssists ->
            players[1].assists?.let { playerTwoAssists ->
                if (playerOneAssists > playerTwoAssists) return playerOneId
                if (playerOneAssists < playerTwoAssists) return playerTwoId
            }
        }

        players[0].penalties?.let { playerOnePenalties ->
            players[1].penalties?.let { playerTwoPenalties ->
                if (playerOnePenalties > playerTwoPenalties) return playerOneId
                if (playerOnePenalties < playerTwoPenalties) return playerTwoId
            }
        }

        return AbsentLiveData.create()
    }

}
