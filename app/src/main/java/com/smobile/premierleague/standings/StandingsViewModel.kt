package com.smobile.premierleague.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.repository.StandingsRepository
import javax.inject.Inject

/**
 * ViewModel for [StandingsFragment]
 */
class StandingsViewModel @Inject constructor(standingsRepository: StandingsRepository) :
    ViewModel() {

    private val leagueId: MutableLiveData<Int> = MutableLiveData()

    val standings: LiveData<Resource<List<Standing>>> = Transformations
        .switchMap(leagueId) {
            standingsRepository.loadStandings(it)
        }

    fun setLeagueId(leagueId: Int) {
        if (this.leagueId.value == leagueId) {
            return
        }

        this.leagueId.value = leagueId
    }

}