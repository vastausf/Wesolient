package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.Message

class ChatAdapterRV2(
    private val onClick: ((Message) -> Unit)? = null
) : ListAdapter<Message, ChatAdapterRV2.ViewHolder>(ScopeDiff) {
    companion object ScopeDiff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Message, newItem: Message) =
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
        fun bind(item: Message, onClick: ((Message) -> Unit)?) {
            itemView.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }
}
