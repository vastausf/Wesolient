package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.client.Frame
import kotlinx.android.synthetic.main.item_server_message.view.*

class ChatAdapterRV(
    private val onClick: ((Frame) -> Unit)? = null,
    private val onLongClick: ((Frame) -> Unit)? = null
) : ListAdapter<Frame, ChatAdapterRV.ViewHolder>(MessageDiff) {
    companion object MessageDiff : DiffUtil.ItemCallback<Frame>() {
        override fun areItemsTheSame(oldItem: Frame, newItem: Frame) =
            oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: Frame, newItem: Frame) =
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
            Frame.Source.SERVER_SOURCE.ordinal -> {
                ServerMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_server_message, parent, false)
                )
            }
            Frame.Source.CLIENT_SOURCE.ordinal -> {
                ClientMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_client_message, parent, false)
                )
            }
            else -> throw IllegalStateException("Illegal message view type")
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(
            item: Frame,
            onClick: ((Frame) -> Unit)?,
            onLongClick: ((Frame) -> Unit)?
        ) {
            itemView.tvMessage.text = item.content

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
}
