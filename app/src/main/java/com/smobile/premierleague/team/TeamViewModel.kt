package com.smobile.premierleague.team

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.PlayerRepository
import com.smobile.premierleague.util.AbsentLiveData
import javax.inject.Inject

/**
 * ViewModel for [TeamFragment]
 */
class TeamViewModel @Inject constructor(playerRepository: PlayerRepository) : ViewModel() {

    companion object {
        const val SEASON = "2019-2020"
    }

    private val teamId: MutableLiveData<Int> = MutableLiveData()
    private val playerOneId: MutableLiveData<Int?> = MutableLiveData()
    private val playerTwoId: MutableLiveData<Int?> = MutableLiveData()

    val players: LiveData<Resource<List<Player>>> = Transformations
        .switchMap(teamId) {
            playerRepository.loadTeam(it, SEASON)
        }

    val playerOne: LiveData<Player> = Transformations
        .switchMap(playerOneId) {
            if (it == null) {
                AbsentLiveData.create()
            } else {
                playerRepository.loadForId(playerId = it)
            }
        }

    val playerTwo: LiveData<Player> = Transformations
        .switchMap(playerTwoId) {
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

    fun clearSelection() {
        playerOneId.value = null
        playerTwoId.value = null
    }

    fun choosePlayer(playerId: Int?): Boolean {
        if (selectPlayer(playerId)) {
            return true
        }

        if (deselectPlayer(playerId)) {
            return true
        }

        return false
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