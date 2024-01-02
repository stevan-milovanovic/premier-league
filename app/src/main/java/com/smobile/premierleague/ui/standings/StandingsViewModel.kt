package com.smobile.premierleague.ui.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.StandingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for [StandingsScreen]
 */

@HiltViewModel
class StandingsViewModel @Inject constructor(
    standingsRepository: StandingsRepository
) : ViewModel() {

    companion object {
        private const val PREMIER_LEAGUE_ID = 2790
    }

    private val leagueId: MutableLiveData<Int> = MutableLiveData()

    val standings: LiveData<Resource<List<Standing>>> = leagueId.switchMap {
        standingsRepository.loadStandings(it)
    }

    init {
        setLeagueId(PREMIER_LEAGUE_ID)
    }

    fun setLeagueId(leagueId: Int) {
        if (this.leagueId.value == leagueId) {
            return
        }

        this.leagueId.value = leagueId
    }

}
