package com.app.batiklens.ui.user.detailMotif

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityDetailMotifBinding
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailMotifActivity : AppCompatActivity() {

    private lateinit var bind: ActivityDetailMotifBinding
    private val detailViewModel: DetailViewModel by viewModel()
    private var isMotifVisible = true
    private var isSejarahVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityDetailMotifBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bind.apply {

            val id = intent.getIntExtra(DETAIL_ID, 0)
            val motifId = intent.getIntExtra(DETAIL_MOTIF_ID, 0)
            val homeId = intent.getIntExtra(HOME_ID, 0)

            if (homeId != 0){
                detailViewModel.detailMotifHome(homeId)
            }

            if (id != 0 && motifId != 0){
               detailViewModel.detailMotif(id, motifId)
            }

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            // Initialize visibility states
            updateSectionVisibility(isMotifVisible, judulArtiMotif, tvArtiMotif)
            updateSectionVisibility(isSejarahVisible, judulSejarahBatik, tvSejarahBatik)

            // Set up click listeners
            judulArtiMotif.setOnClickListener {
                isMotifVisible = !isMotifVisible
                updateSectionVisibility(isMotifVisible, judulArtiMotif, tvArtiMotif)
            }

            judulSejarahBatik.setOnClickListener {
                isSejarahVisible = !isSejarahVisible
                updateSectionVisibility(isSejarahVisible, judulSejarahBatik, tvSejarahBatik)
            }

            detailViewModel.detailMotif.observe(this@DetailMotifActivity) { data ->
                data?.let {
                    Glide.with(this@DetailMotifActivity).load(it.foto).into(imageView2)
                    tvJudul.text = it.namaMotif
                    tvArtikel.text = it.artiMotif
                    tvSejarahBatik.text = it.sejarahBatik
                    tvArtiMotif.text = it.artiMotif
                }
            }

            detailViewModel.detailMotifHome.observe(this@DetailMotifActivity) { data ->
                data?.let {
                    Glide.with(this@DetailMotifActivity).load(it.foto).into(imageView2)
                    tvJudul.text = it.jenis
                    tvSejarahBatik.text = it.sejarah
                    tvArtiMotif.text = it.arti
                }
            }
        }
    }

    private fun updateSectionVisibility(isVisible: Boolean, button: TextView, content: View) {
        val drawableRes = if (isVisible) R.drawable.arrow_up_24 else R.drawable.arrow_down_24
        val rightDrawable = ContextCompat.getDrawable(this, drawableRes)

        button.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null)
        content.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    companion object {
        const val DETAIL_ID = "id"
        const val DETAIL_MOTIF_ID = "motifId"
        const val HOME_ID = "homeId"
    }
}