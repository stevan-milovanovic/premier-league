package com.smobile.premierleague.ui.headtohead

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.di.Injectable
import javax.inject.Inject

/**
 * Fragment for showing head to head statistics
 */
class HeadToHeadFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private val headToHeadViewModel: HeadToHeadViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val playersResource by headToHeadViewModel.players.observeAsState()
            val winnerId by headToHeadViewModel.winnerId.observeAsState()
            playersResource?.let { resource ->
                HeadToHeadScreen(
                    players = resource.data ?: emptyList(),
                    winnerId = winnerId ?: 0
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = HeadToHeadFragmentArgs.fromBundle(requireArguments())
        headToHeadViewModel.setParams(params.playerOneId, params.playerTwoId, params.teamId)
    }

}
