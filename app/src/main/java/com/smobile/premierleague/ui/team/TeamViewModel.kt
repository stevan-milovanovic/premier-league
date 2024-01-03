package com.smobile.premierleague.ui.team

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.smobile.premierleague.Const.SEASON
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.PlayerRepository
import com.smobile.premierleague.util.AbsentLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for [TeamScreen]
 */
@HiltViewModel
class TeamViewModel @Inject constructor(
    playerRepository: PlayerRepository
) : ViewModel() {

    private val teamId: MutableLiveData<Int> = MutableLiveData()
    private val playerOneId: MutableLiveData<Int?> = MutableLiveData()
    private val playerTwoId: MutableLiveData<Int?> = MutableLiveData()

    val players: LiveData<Resource<List<Player>>> = teamId.switchMap {
        playerRepository.loadTeam(it, SEASON)
    }

    val playerOne: LiveData<Player> = playerOneId.switchMap {
        if (it == null) {
            AbsentLiveData.create()
        } else {
            playerRepository.loadForId(playerId = it)
        }
    }

    val playerTwo: LiveData<Player> = playerTwoId.switchMap {
        if (it == null) {
            AbsentLiveData.create()
        } else {
            playerRepository.loadForId(playerId = it)
        }
    }

    fun setTeamId(teamId: Int) {
        if (this.teamId.value == teamId) {
            return
        }

        this.teamId.value = teamId
    }

    fun choosePlayer(playerId: Int?) {
        if (selectPlayer(playerId)) {
            return
        }

        deselectPlayer(playerId)
    }

    private fun selectPlayer(playerId: Int?): Boolean {
        if (playerOne.value == null) {
            if (playerTwoId.value == playerId) {
                return false
            }
            playerOneId.value = playerId
            return true
        }

        if (playerTwo.value == null) {
            if (playerOneId.value == playerId) {
                return false
            }
            playerTwoId.value = playerId
            return true
        }

        return false
    }

    private fun deselectPlayer(playerId: Int?): Boolean {
        if (playerOne.value?.id == playerId) {
            playerOneId.value = null
            return true
        }

        if (playerTwo.value?.id == playerId) {
            playerTwoId.value = null
            return true
        }

        return false
    }

}
