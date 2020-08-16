package com.smobile.premierleague.ui.team

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.Const.SEASON
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for [TeamFragment]
 */
class TeamViewModel @Inject constructor(private val playerRepository: PlayerRepository) : ViewModel() {

    private val teamId: MutableLiveData<Int> = MutableLiveData()
    val playerOne: MutableLiveData<Player?> = MutableLiveData()
    val playerTwo: MutableLiveData<Player?> = MutableLiveData()

    suspend fun loadPlayers(teamId: Int) = withContext(Dispatchers.IO) {
        playerRepository.loadTeam(teamId, SEASON)
    }

    val selectedPlayers: Pair<Int, Int>?
        get() = playerOne.value?.id?.let { playerOneId ->
            playerTwo.value?.id?.let { playerTwoId ->
                Pair(playerOneId, playerTwoId)
            }
        }

    fun setTeamId(teamId: Int) {
        if (this.teamId.value == teamId) {
            return
        }

        this.teamId.value = teamId
    }

    fun choosePlayer(player: Player?): Boolean {
        if (selectPlayer(player)) {
            return true
        }

        if (deselectPlayer(player)) {
            return true
        }

        return false
    }

    private fun selectPlayer(player: Player?): Boolean {
        if (playerOne.value == null) {
            if (playerTwo.value == player) {
                return false
            }
            playerOne.value = player
            return true
        }

        if (playerTwo.value == null) {
            if (playerOne.value == player) {
                return false
            }
            playerTwo.value = player
            return true
        }

        return false
    }

    private fun deselectPlayer(player: Player?): Boolean {
        if (playerOne.value == player) {
            playerOne.value = null
            return true
        }

        if (playerTwo.value == player) {
            playerTwo.value = null
            return true
        }

        return false
    }

}