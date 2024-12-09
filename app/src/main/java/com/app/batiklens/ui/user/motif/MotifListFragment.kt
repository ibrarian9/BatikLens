package com.app.batiklens.ui.user.motif

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.adapters.ListMotifAdapter
import com.app.batiklens.databinding.FragmentMotifListBinding
import com.app.batiklens.ui.user.MainActivity
import com.app.batiklens.ui.user.provinsi.MotifFragment
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel


class MotifListFragment : Fragment() {

    private lateinit var listMotifAdapter: ListMotifAdapter
    private var _binding: FragmentMotifListBinding? = null
    private val bind get() = _binding!!
    private val motifListViewModel: MotifListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            val id = arguments?.getInt(DETAIL_ID, 0)

            id?.let {
                motifListViewModel.listMotif(it)
                listMotifAdapter = ListMotifAdapter(id)
            }

            rv.apply {
                layoutManager = GridLayoutManager(requireActivity(), 2)
                addItemDecoration(SpaceItemDecoration(10))
                adapter = listMotifAdapter
            }

            btnBack.setOnClickListener {
                (activity as MainActivity).loadFragments(MotifFragment())
            }

            motifListViewModel.image.observe(viewLifecycleOwner) { image ->
                image?.let {
                    Glide.with(requireActivity()).load(it).into(bigPict)
                }
            }

            motifListViewModel.provinsi.observe(viewLifecycleOwner) { provinsi ->
                provinsi?.let {
                    textView2.text = provinsi
                }
            }

            motifListViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
                loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            motifListViewModel.listMotif.observe(viewLifecycleOwner) { data ->
                data?.let {
                    listMotifAdapter.submitList(it)
                }
            }
        }
    }

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Get item position
            val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1

            // Apply spacing logic for GridLayoutManager
            outRect.left = space / 2
            outRect.right = space / 2
            outRect.top = if (position < spanCount) space else space / 2
            outRect.bottom = space / 2
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMotifListBinding.inflate(inflater, container, false)
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