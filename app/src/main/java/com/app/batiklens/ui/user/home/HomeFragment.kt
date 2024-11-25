package com.app.batiklens.ui.user.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.batiklens.adapters.ArtikelAdapter
import com.app.batiklens.adapters.MotifHorizontalAdapter
import com.app.batiklens.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val bind get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val artikelAdapter = ArtikelAdapter()
    private val motifAdapter = MotifHorizontalAdapter()

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

            homeViewModel.semuaBerita.observe(viewLifecycleOwner){ listBerita ->
                listBerita?.let {
                    artikelAdapter.submitList(it)
                }
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