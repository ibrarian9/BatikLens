package com.app.batiklens.ui.user.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.batiklens.R
import com.app.batiklens.adapters.ArtikelAdapter
import com.app.batiklens.adapters.ListFashionAdapter
import com.app.batiklens.adapters.MotifHorizontalAdapter
import com.app.batiklens.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val bind get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val artikelAdapter = ArtikelAdapter()
    private val fashionAdapter = ListFashionAdapter()
    private val motifAdapter = MotifHorizontalAdapter()
    private var isSelected = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            // set Up Adapter Motif
            listMotif.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            listMotif.adapter = motifAdapter

            homeViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
                loading.visibility = if (isLoading) View.VISIBLE else View.GONE
                loading2.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            homeViewModel.semuaMotif.observe(viewLifecycleOwner){ listMotif ->
                listMotif?.let {
                    motifAdapter.submitList(it)
                }
            }

            // set Up Adapter Artikel
            listBerita.layoutManager = LinearLayoutManager(requireActivity())
            listBerita.adapter = artikelAdapter

            homeViewModel.semuaBerita.observe(viewLifecycleOwner) { berita ->
                if (isSelected) {
                    berita?.let {
                        listBerita.adapter = artikelAdapter
                        artikelAdapter.submitList(it)
                    }
                }
            }

            tvAll.setOnClickListener {
                if (!isSelected) {
                    isSelected = true
                    updateBackground()
                    homeViewModel.semuaBerita.observe(viewLifecycleOwner){ berita ->
                        berita?.let {
                            listBerita.adapter = artikelAdapter
                            artikelAdapter.submitList(it)
                        }
                    }
                }
            }

            tvFashion.setOnClickListener {
                if (isSelected) {
                    isSelected = false
                    updateBackground()
                    homeViewModel.semuaFashion.observe(viewLifecycleOwner){ listFashion ->
                        listFashion?.let { item ->
                            listBerita.adapter = fashionAdapter
                            fashionAdapter.submitList(item)
                        }
                    }
                }
            }

            cariArtikel.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        artikelAdapter.submitList(emptyList())

                        homeViewModel.semuaArtikel(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun updateBackground() {
        bind.apply {
            if (isSelected) {
                tvAll.setBackgroundResource(R.drawable.button_third_color)
                tvFashion.setBackgroundResource(R.drawable.button_white_color)
            } else {
                tvAll.setBackgroundResource(R.drawable.button_white_color)
                tvFashion.setBackgroundResource(R.drawable.button_third_color)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}