package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.R
import com.vastausf.wesolient.model.data.Message
import kotlinx.android.synthetic.main.item_server_message.view.*

class ChatAdapterRV(
    private val onClick: ((Message) -> Unit)? = null,
    private val onLongClick: ((Message) -> Unit)? = null
) : ListAdapter<Message, ChatAdapterRV.ViewHolder>(MessageDiff) {
    companion object MessageDiff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Message, newItem: Message) =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).source.ordinal
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick, onLongClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Message.Source.SERVER_SOURCE.ordinal -> {
                ServerMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_server_message, parent, false)
                )
            }
            Message.Source.CLIENT_SOURCE.ordinal -> {
                ClientMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_client_message, parent, false)
                )
            }
            Message.Source.SYSTEM_SOURCE.ordinal -> {
                SystemMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_system_message, parent, false)
                )
            }
            else -> throw IllegalStateException("Illegal message view type")
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(
            item: Message,
            onClick: ((Message) -> Unit)?,
            onLongClick: ((Message) -> Unit)?
        ) {
            itemView.messageTV.text = item.content

            itemView.setOnClickListener {
                onClick?.invoke(item)
            }

            itemView.setOnLongClickListener {
                onLongClick?.invoke(item)

                return@setOnLongClickListener true
            }
        }
    }

    inner class ServerMessageViewHolder(itemView: View) : ViewHolder(itemView)

    inner class ClientMessageViewHolder(itemView: View) : ViewHolder(itemView)

    inner class SystemMessageViewHolder(itemView: View) : ViewHolder(itemView)
}
