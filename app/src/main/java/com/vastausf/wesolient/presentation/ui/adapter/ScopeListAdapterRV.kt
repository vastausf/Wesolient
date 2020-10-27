package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.R
import kotlinx.android.synthetic.main.item_scope_list.view.*

class ScopeListAdapterRV(
    private val onClick: (String) -> Unit
) : ListAdapter<String, ScopeListAdapterRV.ViewHolder>(ScopeListDiff) {
    companion object ScopeListDiff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_scope_list, parent, false)
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String, onClick: (String) -> Unit) {
            itemView.title.text = item
            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}
