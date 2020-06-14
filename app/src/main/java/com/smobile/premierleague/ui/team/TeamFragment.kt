package com.smobile.premierleague.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
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

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var binding by autoCleared<FragmentTeamBinding>()
    private var adapter by autoCleared<TeamListAdapter>()

    private val teamViewModel: TeamViewModel by viewModels { viewModelFactory }

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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDataObserver()
        val adapter = TeamListAdapter(
            dataBindingComponent,
            appExecutors,
            teamViewModel
        ) { player ->
            if (!teamViewModel.choosePlayer(player.id)) {
                view?.let {
                    Snackbar.make(
                        it, getString(R.string.players_already_selected),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.playersList.adapter = adapter
        this.adapter = adapter

        binding.compareFab.setOnClickListener {
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
    }

    override fun onResume() {
        super.onResume()
        val params = TeamFragmentArgs.fromBundle(requireArguments())
        teamViewModel.setTeamId(params.teamId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.setLifecycleDestroyed()
    }

    private fun setupDataObserver() {
        teamViewModel.players.observe(viewLifecycleOwner, Observer { result ->
            updateFabVisibility()
            adapter.submitList(result.data)
        })

        teamViewModel.playerOne.observe(viewLifecycleOwner, Observer {
            updateFabVisibility()
        })

        teamViewModel.playerTwo.observe(viewLifecycleOwner, Observer {
            updateFabVisibility()
        })
    }

    private fun updateFabVisibility() {
        teamViewModel.selectedPlayers?.let {
            binding.compareFab.show()
        } ?: run {
            binding.compareFab.hide()
        }
    }

}