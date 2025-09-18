package com.example.assignment.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.databinding.ItemHoldingBinding
import com.example.assignment.domain.model.Holding

class HoldingsAdapter : PagingDataAdapter<Holding, HoldingsAdapter.HoldingViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<Holding>() {
        override fun areItemsTheSame(oldItem: Holding, newItem: Holding): Boolean =
            oldItem.symbol == newItem.symbol

        override fun areContentsTheSame(oldItem: Holding, newItem: Holding): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder =
        HoldingViewHolder(
            ItemHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class HoldingViewHolder(private val binding: ItemHoldingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Holding) {
            binding.tvSymbol.text = item.symbol
            binding.tvNetQtyValue.text = item.quantity.toString()
            val ltpText = UiFormat.formatRupee(item.lastTradedPrice?:0.0)
            binding.tvLtpRightValue.text = ltpText
            val pnl = ((item.lastTradedPrice ?: 0.0) - (item.averagePrice ?: 0.0)) * (item.quantity ?: 0)
            binding.tvPnlValue.text = UiFormat.formatSignedRupee(pnl)
            binding.tvPnlValue.setTextColor(
                if (pnl >= 0) itemView.context.getColor(R.color.pnl_green) else itemView.context.getColor(
                    R.color.pnl_red
                )
            )
        }
    }
}


