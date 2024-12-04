package com.app.batiklens.ui.user.result

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var bind: ActivityResultBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityResultBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bind.apply {

            val resultData = intent.getStringExtra(MOTIF_NAME)
            val namaMotif = intent.getStringExtra(MOTIF)
            val scoreData = intent.getFloatExtra(SCORE, 0f)
            val waktuData = intent.getDoubleExtra(WAKTU, 0.0)

            result.text = "Nama Motif : $resultData"
            motif.text = "Jenis Motif : $namaMotif"
            akurasi.text = "Score Akurasi : $scoreData"
            waktu.text = "Waktu : $waktuData Detik"

        }
    }

    companion object {
        const val MOTIF_NAME = "motifname"
        const val MOTIF = "motif"
        const val SCORE = "score"
        const val WAKTU = "waktu"
    }
}