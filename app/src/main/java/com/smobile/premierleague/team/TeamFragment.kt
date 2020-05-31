package com.smobile.premierleague.team

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.R
import com.smobile.premierleague.binding.FragmentDataBindingComponent
import com.smobile.premierleague.databinding.FragmentTeamBinding
import com.smobile.premierleague.di.Injectable
import com.smobile.premierleague.util.autoCleared
import javax.inject.Inject

/**
 * Fragment to show all players of one team
 */
class TeamFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentTeamBinding>()

    var adapter by autoCleared<TeamListAdapter>()

    lateinit var teamViewModel: TeamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_team,
            container,
            false,
            dataBindingComponent
        )
        binding.playersList.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        teamViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(TeamViewModel::class.java)
        setupDataObserver()
        val adapter = TeamListAdapter(
            dataBindingComponent,
            appExecutors
        ) { player ->
            Log.d("steva", "You have selected: " + player.name)
        }
        binding.playersList.adapter = adapter
        this.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val params = TeamFragmentArgs.fromBundle(requireArguments())
        teamViewModel.setTeamId(params.teamId)
    }

    private fun setupDataObserver() {
        teamViewModel.players.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result.data)
        })
    }

}