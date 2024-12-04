package com.app.batiklens.ui.user.provinsi

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.adapters.ListMotifAdapter
import com.app.batiklens.adapters.ProvinsiAdapter
import com.app.batiklens.databinding.FragmentMotifBinding
import com.app.batiklens.di.models.ListBatikItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class MotifFragment : Fragment() {

    private val adapterProvinsi = ProvinsiAdapter()
    private val motifAdapter = ListMotifAdapter(0)
    private var _binding: FragmentMotifBinding? = null
    private val bind get() = _binding!!
    private val motifViewModel: MotifViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            listMotif.layoutManager = GridLayoutManager(requireActivity(), 2)
            listMotif.addItemDecoration(SpaceItemDecoration(20, 30))
            listMotif.adapter = adapterProvinsi

            motifViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
                loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            motifViewModel.semuaProvinsi.observe(viewLifecycleOwner) { data ->
                data?.let {
                    adapterProvinsi.submitList(it)
                }
            }

            motifViewModel.cariMotif.observe(viewLifecycleOwner) { data ->
                data.let {
                    listMotif.addItemDecoration(SpaceItemDecoration(10, 10))
                    listMotif.adapter = motifAdapter

                    // Map the data into a list of ListBatikItem
                    val listDataMotif = it.map { item ->
                        ListBatikItem(
                            namaMotif = item.namaMotif,
                            foto = item.foto,
                            id = 0,
                            artiMotif = item.artiMotif,
                            sejarahBatik = item.sejarahBatik
                        )
                    }

                    // Submit the entire list to the adapter
                    motifAdapter.submitList(listDataMotif)
                }
            }

            cariMotifBatik.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        adapterProvinsi.submitList(emptyList())

                        motifViewModel.cariMotif(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

    class SpaceItemDecoration(private val space: Int, private val spaceHorizontal: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Get item position
            val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1

            // Apply spacing logic for GridLayoutManager
            outRect.left = spaceHorizontal / 2
            outRect.right = spaceHorizontal / 2
            outRect.top = if (position < spanCount) space else space / 2
            outRect.bottom = space / 2
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMotifBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}