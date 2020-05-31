package com.smobile.premierleague.team

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.PlayerRepository
import javax.inject.Inject

/**
 * ViewModel for [TeamFragment]
 */
class TeamViewModel @Inject constructor(playerRepository: PlayerRepository) : ViewModel() {

    companion object {
        const val SEASON = "2019-2020"
    }

    private val _teamId: MutableLiveData<Int> = MutableLiveData()
    val teamId: LiveData<Int>
        get() = _teamId
    val players: LiveData<Resource<List<Player>>> = Transformations
        .switchMap(teamId) {
            playerRepository.loadTeam(it, SEASON)
        }

    fun setTeamId(teamId: Int) {
        if (_teamId.value == teamId) {
            return
        }

        _teamId.value = teamId
    }

}