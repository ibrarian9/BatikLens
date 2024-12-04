package com.app.batiklens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.R
import com.app.batiklens.databinding.MotifBatikBinding
import com.app.batiklens.di.models.DataLatih
import com.bumptech.glide.Glide

class DataLatihAdapter: ListAdapter<DataLatih, DataLatihAdapter.ViewHolder>(CALLBACK) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MotifBatikBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(getItem(position), position == selectedPosition)

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    fun getSelectedItem(): DataLatih? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            getItem(selectedPosition)
        } else {
            null
        }
    }

    class ViewHolder(
        private val bind: MotifBatikBinding
    ): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: DataLatih, b: Boolean) {
            bind.apply {
                val context = itemView.context

                Glide.with(context).load(item.linkImage).into(ivPoto)
                tvJudul.text = item.namaMotif

                main.setBackgroundColor(
                    if (b) context.getColor(R.color.mainColor)
                    else context.getColor(R.color.white)
                )
            }
        }
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<DataLatih>() {
            override fun areItemsTheSame(
                oldItem: DataLatih,
                newItem: DataLatih
            ): Boolean {
                return oldItem.namaMotif == newItem.namaMotif
            }

            override fun areContentsTheSame(
                oldItem: DataLatih,
                newItem: DataLatih
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}