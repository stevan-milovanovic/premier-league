package com.smobile.premierleague.ui.standings

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.R
import com.smobile.premierleague.binding.FragmentDataBindingComponent
import com.smobile.premierleague.databinding.FragmentStandingsBinding
import com.smobile.premierleague.di.Injectable
import com.smobile.premierleague.util.autoCleared
import javax.inject.Inject

/**
 * Fragment for showing current standings in chosen league
 */
class StandingsFragment : Fragment(), Injectable {

    companion object {
        private const val PREMIER_LEAGUE_ID = 524
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var binding by autoCleared<FragmentStandingsBinding>()
    private var adapter by autoCleared<StandingsListAdapter>()

    private val standingsViewModel: StandingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_standings,
            container,
            false,
            dataBindingComponent
        )

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDataObserver()
        val adapter = StandingsListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) { standing ->
            findNavController().navigate(StandingsFragmentDirections.showTeam(standing.id))
        }
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.standingsList.addItemDecoration(dividerItemDecoration)
        binding.standingsList.adapter = adapter
        this.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        standingsViewModel.setLeagueId(PREMIER_LEAGUE_ID)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun setupDataObserver() {
        standingsViewModel.standings.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
        })
    }

}