package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.databinding.ItemTemplateBinding

class TemplateListAdapterRV(
    private val onClick: ((Template, View) -> Unit)? = null,
    private val onLongClick: ((Template, View) -> Unit)? = null
) : ListAdapter<Template, TemplateListAdapterRV.ViewHolder>(Diff) {
    companion object Diff : DiffUtil.ItemCallback<Template>() {
        override fun areItemsTheSame(oldItem: Template, newItem: Template) =
            oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: Template, newItem: Template) =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick, onLongClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(
            ItemTemplateBinding.inflate(layoutInflater, parent, false)
        )
    }

    inner class ViewHolder(
        private val binding: ItemTemplateBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Template,
            onClick: ((Template, View) -> Unit)?,
            onLongClick: ((Template, View) -> Unit)?
        ) {
            binding.tvTemplateTitle.text = item.title
            binding.tvTemplateMessage.text = item.message

            binding.root.setOnClickListener {
                onClick?.invoke(item, itemView)
            }

            binding.root.setOnLongClickListener {
                onLongClick?.invoke(item, itemView)

                true
            }
        }
    }
}
