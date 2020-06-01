package com.smobile.premierleague.headtohead

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.PremierLeagueApp.Companion.SEASON
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

        this.playerOneId.value = playerOneId
        this.playerTwoId.value = playerTwoId
        this.teamId.value = teamId
    }

    val winnerId: LiveData<Int> = Transformations
        .switchMap(players) { players ->
            playerOneId
        }

}