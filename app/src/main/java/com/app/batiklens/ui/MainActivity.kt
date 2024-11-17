package com.app.batiklens.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityMainBinding
import com.app.batiklens.ui.history.HistoryFragment
import com.app.batiklens.ui.home.HomeFragment
import com.app.batiklens.ui.motif.MotifFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null){
            loadFragments(HomeFragment())
        }

        bind.apply {
            botNavbar.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        loadFragments(HomeFragment())
                        true
                    }
                    R.id.nav_motif -> {
                        loadFragments(MotifFragment())
                        true
                    }
                    R.id.nav_history -> {
                        loadFragments(HistoryFragment())
                        true
                    }
                    R.id.nav_profile -> {
                        true
                    }
                    else -> false
                }
            }

            savedInstanceState?.let {
                botNavbar.selectedItemId = it.getInt(SELECTED_NAV_ITEM, R.id.nav_home)
            } ?: run {
                botNavbar.selectedItemId = R.id.nav_home
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_NAV_ITEM, bind.botNavbar.selectedItemId)
    }

    private fun loadFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addToBackStack(null)
            commit()
        }
    }

    companion object {
        private const val SELECTED_NAV_ITEM = "selectNavItem"
    }
}