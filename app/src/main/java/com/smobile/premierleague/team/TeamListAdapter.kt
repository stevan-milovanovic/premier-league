package com.smobile.premierleague.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.R
import com.smobile.premierleague.databinding.PlayerItemBinding
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.ui.common.DataBoundListAdapter

class TeamListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickHandler: ((Player) -> Unit)?
) : DataBoundListAdapter<Player, PlayerItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player) =
            oldItem.teamId == newItem.teamId && oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Player, newItem: Player) =
            oldItem.name == newItem.name && oldItem.position == newItem.position
    }
) {
    override fun createBinding(parent: ViewGroup): PlayerItemBinding {
        val binding = DataBindingUtil.inflate<PlayerItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.player_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.player?.let {
                clickHandler?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: PlayerItemBinding, item: Player) {
        binding.player = item
    }
}