package com.smobile.premierleague.ui.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.R
import com.smobile.premierleague.di.Injectable
import javax.inject.Inject

/**
 * Fragment for showing current standings in chosen league
 */
class StandingsFragment : Fragment(), Injectable {

    companion object {
        private const val PREMIER_LEAGUE_ID = 2790
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private val standingsViewModel: StandingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            val standings by standingsViewModel.standings.observeAsState()
            standings?.let { resource ->
                if (resource.data != null) {
                    StandingsScreen(standings = resource.data) { teamId ->
                        showSelectedTeam(teamId)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    override fun onResume() {
        super.onResume()
        standingsViewModel.setLeagueId(PREMIER_LEAGUE_ID)
    }

    private fun showSelectedTeam(teamId: Int) {
        findNavController().navigate(StandingsFragmentDirections.showTeam(teamId))
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}