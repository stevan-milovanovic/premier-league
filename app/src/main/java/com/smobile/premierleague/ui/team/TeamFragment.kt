package com.smobile.premierleague.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.smobile.premierleague.di.Injectable
import javax.inject.Inject

/**
 * Fragment to show all players of one team
 */
class TeamFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val teamViewModel: TeamViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            val playersResource by teamViewModel.players.observeAsState()
            val playerOne by teamViewModel.playerOne.observeAsState()
            val playerTwo by teamViewModel.playerTwo.observeAsState()
            playersResource?.let { resource ->
                TeamScreen(
                    players = resource.data ?: emptyList(),
                    playerOne = playerOne,
                    playerTwo = playerTwo,
                    onPlayerSelected = { teamViewModel.choosePlayer(it.id) },
                    onComparePlayersClicked = ::navigateToHeadToHeadScreen
                )
            }
        }
    }

    private fun navigateToHeadToHeadScreen() {
        teamViewModel.selectedPlayers?.let { players ->
            val params = TeamFragmentArgs.fromBundle(requireArguments())
            findNavController().navigate(
                TeamFragmentDirections.showHeadToHead(
                    players.first,
                    players.second,
                    params.teamId
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val params = TeamFragmentArgs.fromBundle(requireArguments())
        teamViewModel.setTeamId(params.teamId)
    }

}
