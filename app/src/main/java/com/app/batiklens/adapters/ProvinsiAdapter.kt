package com.app.batiklens.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.R
import com.app.batiklens.databinding.MotifDaerahViewBinding
import com.app.batiklens.di.models.ProvinsiMotifModelItem
import com.app.batiklens.ui.user.motif.MotifListFragment
import com.bumptech.glide.Glide

class ProvinsiAdapter: ListAdapter<ProvinsiMotifModelItem, ProvinsiAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MotifDaerahViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(getItem(position))
    }

    class ViewHolder(
        private val bind: MotifDaerahViewBinding
    ): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: ProvinsiMotifModelItem) {
            bind.apply {
                val context = itemView.context

                Glide.with(context).load(item.foto).into(image)
                tvNama.text = item.provinsi

                itemView.setOnClickListener {
                    val fragment = MotifListFragment().apply {
                        arguments = Bundle().apply {
                            putInt(MotifListFragment.DETAIL_ID, item.id)
                        }
                    }

                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProvinsiMotifModelItem>() {
            override fun areItemsTheSame(
                oldItem: ProvinsiMotifModelItem,
                newItem: ProvinsiMotifModelItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProvinsiMotifModelItem,
                newItem: ProvinsiMotifModelItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}


