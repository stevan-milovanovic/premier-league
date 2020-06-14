package com.smobile.premierleague.ui.headtohead

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.R
import com.smobile.premierleague.databinding.PlayerDetailsItemBinding
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.ui.common.DataBoundListAdapter

class HeadToHeadAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val viewModel: HeadToHeadViewModel
) : DataBoundListAdapter<Player, PlayerDetailsItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player) =
            oldItem.teamId == newItem.teamId && oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Player, newItem: Player) =
            oldItem.name == newItem.name && oldItem.position == newItem.position &&
                    oldItem.goals == newItem.goals && oldItem.passes == newItem.passes
    }
) {
    override fun createBinding(parent: ViewGroup): PlayerDetailsItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.player_details_item,
            parent,
            false,
            dataBindingComponent
        )
    }

    override fun bind(binding: PlayerDetailsItemBinding, item: Player) {
        binding.player = item
        binding.viewModel = viewModel
    }
}