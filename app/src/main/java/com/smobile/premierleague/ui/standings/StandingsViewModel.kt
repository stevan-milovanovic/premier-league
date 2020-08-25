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

    fun getStandings(leagueId: Int) = standingsRepository.loadStandings(leagueId)

}