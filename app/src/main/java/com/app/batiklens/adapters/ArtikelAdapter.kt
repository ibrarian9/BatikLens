package com.app.batiklens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.databinding.ArtikelListViewBinding
import com.app.batiklens.di.models.ArtikelModelItem
import com.bumptech.glide.Glide

class ArtikelAdapter: ListAdapter<ArtikelModelItem, ArtikelAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ArtikelListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(getItem(position))
    }

    class ViewHolder(
        private val bind: ArtikelListViewBinding
    ): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: ArtikelModelItem) {
            bind.apply {
                Glide.with(itemView.context).load(item.foto).into(fotoArtikel)
                tvJudul.text = item.judul
                tvArtikel.text = item.deskripsi
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArtikelModelItem>() {
            override fun areItemsTheSame(
                oldItem: ArtikelModelItem,
                newItem: ArtikelModelItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ArtikelModelItem,
                newItem: ArtikelModelItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}