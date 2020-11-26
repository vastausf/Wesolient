package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.data.client.Frame
import com.vastausf.wesolient.databinding.ItemClientMessageBinding
import com.vastausf.wesolient.databinding.ItemServerMessageBinding

class ChatAdapterRV(
    private val onClick: ((Frame) -> Unit)? = null,
    private val onLongClick: ((Frame) -> Unit)? = null
) : ListAdapter<Frame, ChatAdapterRV.ViewHolder>(Diff) {
    companion object Diff : DiffUtil.ItemCallback<Frame>() {
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
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            Frame.Source.SERVER_SOURCE.ordinal -> {
                ServerMessageViewHolder(
                    ItemServerMessageBinding.inflate(layoutInflater, parent, false)
                )
            }
            Frame.Source.CLIENT_SOURCE.ordinal -> {
                ClientMessageViewHolder(
                    ItemClientMessageBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> throw IllegalStateException("Illegal message view type")
        }
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(
            item: Frame,
            onClick: ((Frame) -> Unit)?,
            onLongClick: ((Frame) -> Unit)?
        )
    }

    inner class ServerMessageViewHolder(
        private val binding: ItemServerMessageBinding
    ) : ViewHolder(binding.root) {
        override fun bind(
            item: Frame,
            onClick: ((Frame) -> Unit)?,
            onLongClick: ((Frame) -> Unit)?
        ) {
            binding.tvMessage.text = item.content

            binding.root.setOnClickListener {
                onClick?.invoke(item)
            }

            binding.root.setOnLongClickListener {
                onLongClick?.invoke(item)

                return@setOnLongClickListener true
            }
        }
    }

    inner class ClientMessageViewHolder(
        private val binding: ItemClientMessageBinding
    ) : ViewHolder(binding.root) {
        override fun bind(
            item: Frame,
            onClick: ((Frame) -> Unit)?,
            onLongClick: ((Frame) -> Unit)?
        ) {
            binding.tvMessage.text = item.content

            binding.root.setOnClickListener {
                onClick?.invoke(item)
            }

            binding.root.setOnLongClickListener {
                onLongClick?.invoke(item)

                return@setOnLongClickListener true
            }
        }
    }
}
