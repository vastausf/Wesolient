package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Template
import kotlinx.android.synthetic.main.item_template.view.*

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
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Template,
            onClick: ((Template, View) -> Unit)?,
            onLongClick: ((Template, View) -> Unit)?
        ) {
            itemView.tvTitle_ItemTemplate.text = item.title
            itemView.tvMessage_ItemTemplate.text = item.message

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
