package com.smobile.premierleague.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentStandingsBinding>()

    var adapter by autoCleared<StandingsListAdapter>()

    lateinit var standingsViewModel: StandingsViewModel

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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        standingsViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StandingsViewModel::class.java)
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

    private fun setupDataObserver() {
        standingsViewModel.standings.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
        })
    }

}