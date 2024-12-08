package com.app.batiklens.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.databinding.MotifHomeViewBinding
import com.app.batiklens.di.models.MotifModelItem
import com.app.batiklens.ui.user.detailMotif.DetailMotifActivity
import com.bumptech.glide.Glide

class MotifHorizontalAdapter: ListAdapter<MotifModelItem, MotifHorizontalAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(MotifHomeViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    class ViewHolder(
        private val bind: MotifHomeViewBinding
    ): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: MotifModelItem) {
            bind.apply {
                val context = itemView.context

                Glide.with(context).load(item.foto).into(fotoMotif)
                namaMotif.text = item.jenis

                itemView.setOnClickListener {
                    val i = Intent(context, DetailMotifActivity::class.java).apply {
                        putExtra(DetailMotifActivity.HOME_ID, item.id)
                    }
                    context.startActivity(i)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MotifModelItem>() {
            override fun areItemsTheSame(
                oldItem: MotifModelItem,
                newItem: MotifModelItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MotifModelItem,
                newItem: MotifModelItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}