package com.smobile.premierleague.headtohead

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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.R
import com.smobile.premierleague.binding.FragmentDataBindingComponent
import com.smobile.premierleague.databinding.FragmentHeadToHeadBinding
import com.smobile.premierleague.di.Injectable
import com.smobile.premierleague.util.autoCleared
import javax.inject.Inject

/**
 * Fragment for showing head to head statistics
 */
class HeadToHeadFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentHeadToHeadBinding>()

    var adapter by autoCleared<HeadToHeadAdapter>()

    lateinit var headToHeadViewModel: HeadToHeadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_head_to_head,
            container,
            false,
            dataBindingComponent
        )
        binding.headToHeadList.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        headToHeadViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(HeadToHeadViewModel::class.java)
        setupDataObserver()
        val adapter = HeadToHeadAdapter(
            dataBindingComponent,
            appExecutors,
            headToHeadViewModel
        )
        binding.headToHeadList.adapter = adapter
        this.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val params = HeadToHeadFragmentArgs.fromBundle(requireArguments())
        headToHeadViewModel.setParams(params.playerOneId, params.playerTwoId, params.teamId)
    }

    private fun setupDataObserver() {
        headToHeadViewModel.players.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result.data)
        })
    }

}