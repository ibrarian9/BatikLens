package com.app.batiklens.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.databinding.MotifBatikBinding
import com.app.batiklens.di.models.ListBatikItem
import com.app.batiklens.ui.user.detailMotif.DetailMotifActivity
import com.bumptech.glide.Glide

class ListMotifAdapter(
    private var id: Int
) : ListAdapter<ListBatikItem, ListMotifAdapter.ViewHolder>(CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MotifBatikBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(getItem(position), id)
    }

    class ViewHolder(
        private val bind: MotifBatikBinding
    ): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: ListBatikItem, id: Int) {
            bind.apply {
                val context = itemView.context

                Glide.with(context).load(item.foto).into(ivPoto)
                tvJudul.text = item.namaMotif

                itemView.setOnClickListener {
                    val i = Intent(context, DetailMotifActivity::class.java).apply {
                        putExtra(DetailMotifActivity.DETAIL_ID, id)
                        putExtra(DetailMotifActivity.DETAIL_MOTIF_ID, item.id)
                    }
                    context.startActivity(i)
                }
            }
        }
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<ListBatikItem>() {
            override fun areItemsTheSame(
                oldItem: ListBatikItem,
                newItem: ListBatikItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListBatikItem,
                newItem: ListBatikItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}