package com.smobile.premierleague.ui.headtohead

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.Const.SEASON
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.PlayerRepository
import com.smobile.premierleague.util.AbsentLiveData
import javax.inject.Inject

/**
 * ViewModel for [HeadToHeadFragment]
 */
class HeadToHeadViewModel @Inject constructor(playerRepository: PlayerRepository) : ViewModel() {

    private val teamId: MutableLiveData<Int> = MutableLiveData()
    private val playerOneId: MutableLiveData<Int> = MutableLiveData()
    private val playerTwoId: MutableLiveData<Int> = MutableLiveData()

    val players: LiveData<Resource<List<Player>>> = Transformations
        .switchMap(teamId) { teamId ->
            playerOneId.value?.let { playerOneId ->
                playerTwoId.value?.let { playerTwoId ->
                    if (playerOneId != 0 && playerTwoId != 0) {
                        playerRepository.loadHeadToHeadStatistics(
                            playerOneId,
                            playerTwoId,
                            teamId,
                            SEASON
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

    val winnerId: LiveData<Int> = Transformations
        .switchMap(players) { resource ->
            resource.data?.let { players ->
                comparePlayers(players)
            }
        }

    private fun comparePlayers(players: List<Player>): LiveData<Int> {
        if (players.size != 2) return AbsentLiveData.create()

        players[0].goals?.let { playerOneGoals ->
            players[1].goals?.let { playerTwoGoals ->
                if (playerOneGoals.total > playerTwoGoals.total) return playerOneId
                if (playerOneGoals.total < playerTwoGoals.total) return playerTwoId

                if (playerOneGoals.assists > playerTwoGoals.assists) return playerOneId
                if (playerOneGoals.assists < playerTwoGoals.assists) return playerTwoId

                players[0].passes?.let { playerOnePasses ->
                    players[1].passes?.let { playerTwoPasses ->
                        if (playerOnePasses.total > playerTwoPasses.total) return playerOneId
                        if (playerOnePasses.total < playerTwoPasses.total) return playerTwoId
                    }
                }
            }
        }

        return AbsentLiveData.create()
    }

}