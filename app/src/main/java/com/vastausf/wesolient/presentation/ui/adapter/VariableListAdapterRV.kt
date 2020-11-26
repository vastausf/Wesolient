package com.vastausf.wesolient.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.databinding.ItemVariableBinding

class VariableListAdapterRV(
    private val onClick: ((Variable, View) -> Unit)? = null,
    private val onLongClick: ((Variable, View) -> Unit)? = null
) : ListAdapter<Variable, VariableListAdapterRV.ViewHolder>(Diff) {
    companion object Diff : DiffUtil.ItemCallback<Variable>() {
        override fun areItemsTheSame(oldItem: Variable, newItem: Variable) =
            oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: Variable, newItem: Variable) =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick, onLongClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(
            ItemVariableBinding.inflate(layoutInflater, parent, false)
        )
    }

    inner class ViewHolder(
        private val binding: ItemVariableBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Variable,
            onClick: ((Variable, View) -> Unit)?,
            onLongClick: ((Variable, View) -> Unit)?
        ) {
            binding.tvVariableTitle.text = item.title
            binding.tvVariableValue.text = item.value

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
