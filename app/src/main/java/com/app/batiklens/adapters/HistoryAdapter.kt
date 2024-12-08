package com.app.batiklens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.databinding.HistoryViewBinding
import com.app.batiklens.di.database.History
import com.bumptech.glide.Glide

class HistoryAdapter : ListAdapter<History, HistoryAdapter.ViewHolder>(DIFF_CALBACK) {

    class ViewHolder(private val binding: HistoryViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History){
            val context = itemView.context

            binding.tvJudul.text = history.namaBatik
            binding.tvTanggal.text = history.timestamp.toString()
            Glide.with(context).load(history.imageUri.toUri()).into(binding.ivPoto)
            binding.btnDelete.setOnClickListener {
                
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    companion object {
        val DIFF_CALBACK = object : DiffUtil.ItemCallback<History>(){
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}