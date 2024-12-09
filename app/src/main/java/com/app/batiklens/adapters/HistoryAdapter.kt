package com.app.batiklens.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.R
import com.app.batiklens.databinding.HistoryViewBinding
import com.app.batiklens.di.database.History
import com.app.batiklens.ui.user.detailMotif.DetailMotifActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
    private val onDeleteClick: (History) -> Unit
): ListAdapter<History, HistoryAdapter.ViewHolder>(DIFF_CALBACK) {

    class ViewHolder(private val binding: HistoryViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History, onDeleteClick: (History) -> Unit){
            val context = itemView.context
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(history.timestamp)

            binding.apply {
                tvTanggal.text = formattedDate
                tvJudul.text = history.namaBatik
                Glide.with(context).load(history.imageUri.toUri()).error(R.drawable.photo_putih).into(ivPoto)
                btnDelete.setOnClickListener {
                    onDeleteClick(history)
                }

                itemView.setOnClickListener {
                    val i = Intent(context, DetailMotifActivity::class.java).apply {
                        putExtra(DetailMotifActivity.DETAIL_ID, history.idProvinsi)
                        putExtra(DetailMotifActivity.DETAIL_MOTIF_ID, history.idMotif)
                    }
                    context.startActivity(i)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history, onDeleteClick)
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