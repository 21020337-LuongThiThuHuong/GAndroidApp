package com.example.androidproject.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.databinding.ProgressItemBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var historyList: List<HistoryItem> = listOf()

    inner class ViewHolder(
        private val binding: ProgressItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            historyItem: HistoryItem,
            position: Int,
        ) {
            binding.progressItemDescription.text = historyItem.description
            binding.progressItemIcon.setImageResource(
                if (historyItem.isUp) {
                    binding.progressItemTitle.text = "up!"
                    binding.progressItemTitle.setTextColor(
                        binding.progressItemTitle.context.getColor(R.color.green),
                    )
                    R.drawable.noun_next_step
                } else {
                    binding.progressItemTitle.text = "down!"
                    binding.progressItemTitle.setTextColor(
                        binding.progressItemTitle.context.getColor(R.color.red),
                    )
                    R.drawable.noun_fall
                },
            )

            binding.progressItemLine.visibility = if (position == 0) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ProgressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(historyList[position], position)
    }

    override fun getItemCount(): Int = historyList.size

    fun submitList(list: List<HistoryItem>) {
        historyList = list
        notifyDataSetChanged()
    }
}
