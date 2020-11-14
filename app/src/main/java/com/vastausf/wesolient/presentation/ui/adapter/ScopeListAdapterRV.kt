package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.R
import com.vastausf.wesolient.model.data.Scope
import kotlinx.android.synthetic.main.item_scope_list.view.*

class ScopeListAdapterRV(
    private val onClick: ((Scope, View) -> Unit)? = null,
    private val onLongClick: ((Scope, View) -> Unit)? = null
) : ListAdapter<Scope, ScopeListAdapterRV.ViewHolder>(ScopeDiff) {
    companion object ScopeDiff : DiffUtil.ItemCallback<Scope>() {
        override fun areItemsTheSame(oldItem: Scope, newItem: Scope) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Scope, newItem: Scope) =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick, onLongClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_scope_list, parent, false)
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Scope,
            onClick: ((Scope, View) -> Unit)?,
            onLongClick: ((Scope, View) -> Unit)?
        ) {
            itemView.title.text = item.title

            itemView.setOnClickListener {
                onClick?.invoke(item, itemView)
            }

            itemView.setOnLongClickListener {
                onLongClick?.invoke(item, itemView)

                true
            }
        }
    }
}
