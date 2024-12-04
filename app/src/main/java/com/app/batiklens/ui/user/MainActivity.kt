package com.app.batiklens.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityMainBinding
import com.app.batiklens.ui.user.compare.CompareModelFragment
import com.app.batiklens.ui.user.home.HomeFragment
import com.app.batiklens.ui.user.profil.ProfileFragment
import com.app.batiklens.ui.user.provinsi.MotifFragment
import com.app.batiklens.ui.user.scanBatik.ScannerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottomNavigationAppBar)) { _, insets ->
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
                        loadFragments(CompareModelFragment())
                        true
                    }
                    R.id.nav_profile -> {
                        loadFragments(ProfileFragment())
                        true
                    }
                    else -> false
                }
            }

            bind.fab.setOnClickListener {
                startActivity(Intent(this@MainActivity, ScannerActivity::class.java))
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

    fun loadFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            disallowAddToBackStack()
            commit()
        }
    }

    companion object {
        private const val SELECTED_NAV_ITEM = "selectNavItem"
    }
}