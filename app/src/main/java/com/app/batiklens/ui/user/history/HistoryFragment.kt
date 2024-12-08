package com.app.batiklens.ui.user.history

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.adapters.HistoryAdapter
import com.app.batiklens.databinding.FragmentHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val bind get() = _binding!!
    private val historyAdapter = HistoryAdapter()
    private val historyViewModel: HistoryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {

            listHistory.apply {
                layoutManager = GridLayoutManager(requireActivity(), 2)
                addItemDecoration(SpaceItemDecoration(10))
                adapter = historyAdapter
            }

            historyViewModel.getAllHistory().observe(viewLifecycleOwner) { list ->
                list?.let {
                    historyAdapter.submitList(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return bind.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}