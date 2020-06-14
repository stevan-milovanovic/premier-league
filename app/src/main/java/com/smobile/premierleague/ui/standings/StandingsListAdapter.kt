package com.smobile.premierleague.ui.standings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.smobile.premierleague.AppExecutors
import com.smobile.premierleague.R
import com.smobile.premierleague.databinding.StandingItemBinding
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.ui.common.DataBoundListAdapter

/**
 * A RecyclerView adapter for [Standing] class.
 */
class StandingsListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickHandler: ((Standing) -> Unit)?
) : DataBoundListAdapter<Standing, StandingItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Standing>() {
        override fun areItemsTheSame(oldItem: Standing, newItem: Standing) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Standing, newItem: Standing) =
            oldItem.name == newItem.name && oldItem.logo == newItem.logo
    }
) {
    override fun createBinding(parent: ViewGroup): StandingItemBinding {
        val binding = DataBindingUtil.inflate<StandingItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.standing_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.standing?.let {
                clickHandler?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: StandingItemBinding, item: Standing) {
        binding.standing = item
    }
}