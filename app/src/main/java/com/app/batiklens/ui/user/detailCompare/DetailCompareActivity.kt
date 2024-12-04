package com.app.batiklens.ui.user.detailCompare

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.batiklens.R
import com.app.batiklens.adapters.ListModelAdapter
import com.app.batiklens.databinding.ActivityDetailCompareBinding
import com.app.batiklens.di.models.PredictionResult
import com.bumptech.glide.Glide

class DetailCompareActivity : AppCompatActivity() {

    private lateinit var bind: ActivityDetailCompareBinding
    private val listModelAdapter = ListModelAdapter()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityDetailCompareBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bind.apply {

            val results: ArrayList<PredictionResult>? = intent.getParcelableArrayListExtra(RESULT_COMPARE)
            val summary: String? = intent.getStringExtra(RESULT_FAST)
            val image = intent.getIntExtra(IMAGE_URI, 0)
            val namaMotif = intent.getStringExtra(NAME_MOTIF)

            listModel.layoutManager = LinearLayoutManager(this@DetailCompareActivity)
            listModel.adapter = listModelAdapter

            results?.let { data ->
                val sortedList = data.sortedByDescending { it.confidance.toDoubleOrNull() ?: 0.0 }
                listModelAdapter.submitList(sortedList)
            }

            summary?.let {
                tvModel.text = it
            }

            namaMotif?.let {
                motifName.text = "Nama Motif : $it"
            }

            if (image != 0) {
                Glide.with(this@DetailCompareActivity).load(image).into(ivPoto)
            }

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

        }
    }

    companion object {
        const val NAME_MOTIF = "name_Motif"
        const val IMAGE_URI = "image_uri"
        const val RESULT_COMPARE = "result_compare"
        const val RESULT_FAST = "result_fastest"
    }
}