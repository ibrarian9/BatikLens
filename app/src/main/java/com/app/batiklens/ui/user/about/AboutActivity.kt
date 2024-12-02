package com.app.batiklens.ui.user.about

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var bind: ActivityAboutBinding
    private var isBatikLensShow = false
    private var isMotifInsfoShow = false
    private var isTutorialShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize visibility states
        updateSectionVisibility(isBatikLensShow, bind.batikLens, bind.tvBatikLens)
        updateSectionVisibility(isMotifInsfoShow, bind.motifBatik, bind.tvMotifBatik)
        updateSectionVisibility(isTutorialShow, bind.tutorial, bind.tvTutorial)

        // Set up click listeners
        bind.apply {
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            batikLens.setOnClickListener {
                isBatikLensShow = !isBatikLensShow
                updateSectionVisibility(isBatikLensShow, batikLens, tvBatikLens)
            }

            motifBatik.setOnClickListener {
                isMotifInsfoShow = !isMotifInsfoShow
                updateSectionVisibility(isMotifInsfoShow, motifBatik, tvMotifBatik)
            }

            tutorial.setOnClickListener {
                isTutorialShow = !isTutorialShow
                updateSectionVisibility(isTutorialShow, tutorial, tvTutorial)
            }
        }
    }

    private fun updateSectionVisibility(isVisible: Boolean, button: TextView, content: View) {
        val drawableRes = if (isVisible) R.drawable.arrow_up_24 else R.drawable.arrow_down_24
        val rightDrawable = ContextCompat.getDrawable(this, drawableRes)

        button.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null)
        content.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}