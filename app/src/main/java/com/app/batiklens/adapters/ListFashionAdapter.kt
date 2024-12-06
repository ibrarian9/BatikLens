package com.app.batiklens.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.R
import com.app.batiklens.databinding.ArtikelListViewBinding
import com.app.batiklens.di.models.FashionModelsItem
import com.app.batiklens.ui.user.berita.BeritaFragment
import com.bumptech.glide.Glide

class ListFashionAdapter: ListAdapter<FashionModelsItem, ListFashionAdapter.ViewHolder>(CALLBACK) {

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
        fun binding(item: FashionModelsItem) {
            bind.apply {
                val context = itemView.context

                Glide.with(context).load(item.foto).into(fotoArtikel)
                tvJudul.text = item.judul
                tvArtikel.text = item.deskripsi

                itemView.setOnClickListener {
                    val fragment = BeritaFragment().apply {
                        arguments = Bundle().apply {
                            putInt(BeritaFragment.DETAIL_FASHION_ID, item.id)
                        }
                    }

                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, fragment)
                        .disallowAddToBackStack()
                        .commit()
                }
            }
        }
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<FashionModelsItem>() {
            override fun areItemsTheSame(
                oldItem: FashionModelsItem,
                newItem: FashionModelsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FashionModelsItem,
                newItem: FashionModelsItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}

