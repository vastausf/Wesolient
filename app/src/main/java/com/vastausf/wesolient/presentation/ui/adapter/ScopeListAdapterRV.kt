package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.databinding.ItemScopeListBinding

class ScopeListAdapterRV(
    private val onClick: ((Scope, View) -> Unit)? = null,
    private val onLongClick: ((Scope, View) -> Unit)? = null
) : ListAdapter<Scope, ScopeListAdapterRV.ViewHolder>(ScopeDiff) {
    companion object ScopeDiff : DiffUtil.ItemCallback<Scope>() {
        override fun areItemsTheSame(oldItem: Scope, newItem: Scope) =
            oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: Scope, newItem: Scope) =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick, onLongClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(
            ItemScopeListBinding.inflate(layoutInflater, parent, false)
        )
    }

    inner class ViewHolder(
        private val binding: ItemScopeListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Scope,
            onClick: ((Scope, View) -> Unit)?,
            onLongClick: ((Scope, View) -> Unit)?
        ) {
            binding.tvScopeTitle.text = item.title

            binding.root.setOnClickListener {
                onClick?.invoke(item, binding.root)
            }

            binding.root.setOnLongClickListener {
                onLongClick?.invoke(item, binding.root)

                true
            }
        }
    }
}
