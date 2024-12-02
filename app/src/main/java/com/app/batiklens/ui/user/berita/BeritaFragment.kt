package com.app.batiklens.ui.user.berita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.batiklens.databinding.FragmentBeritaBinding
import com.app.batiklens.ui.user.MainActivity
import com.app.batiklens.ui.user.home.HomeFragment
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class BeritaFragment : Fragment() {

    private var _binding: FragmentBeritaBinding? = null
    private val bind get() = _binding!!
    private val beritaViewModel: BeritaViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            val id = arguments?.getInt(DETAIL_ID, 0)

            id?.let {
                beritaViewModel.berita(it)
            }

            btnBack.setOnClickListener {
                (activity as MainActivity).loadFragments(HomeFragment())
            }

            beritaViewModel.beritaData.observe(viewLifecycleOwner) { data ->
                data?.let {
                    Glide.with(requireActivity()).load(it.foto).into(ivPoto)
                    tvJudul.text = it.judul
                    tvNama.text = it.author
                    tvTanggal.text = it.tanggal
                    tvArtikel.text = it.deskripsi
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBeritaBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DETAIL_ID = "id"
    }

}