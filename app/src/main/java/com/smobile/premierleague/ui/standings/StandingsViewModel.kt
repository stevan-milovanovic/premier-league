package com.smobile.premierleague.ui.standings

import androidx.lifecycle.ViewModel
import com.smobile.premierleague.repository.StandingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for [StandingsFragment]
 */
class StandingsViewModel @Inject constructor(private val standingsRepository: StandingsRepository) :
    ViewModel() {

    suspend fun getStandings(leagueId: Int) = withContext(Dispatchers.IO) {
        standingsRepository.loadStandings(leagueId)
    }

}