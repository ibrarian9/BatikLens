package com.app.batiklens.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.databinding.ItemCompareResultBinding
import com.app.batiklens.di.models.PredictionResult

class ListModelAdapter: ListAdapter<PredictionResult, ListModelAdapter.ViewHolder>(CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCompareResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(getItem(position))
    }

    class ViewHolder(
        private val bind: ItemCompareResultBinding
    ): RecyclerView.ViewHolder(bind.root) {
        @SuppressLint("SetTextI18n")
        fun binding(item: PredictionResult) {
            bind.apply {
                tvModel.text = item.nameModul
                tvConfidance.text = item.confidance
            }
        }
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<PredictionResult>() {
            override fun areItemsTheSame(
                oldItem: PredictionResult,
                newItem: PredictionResult
            ): Boolean {
                return oldItem.nameModul == newItem.nameModul
            }

            override fun areContentsTheSame(
                oldItem: PredictionResult,
                newItem: PredictionResult
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}