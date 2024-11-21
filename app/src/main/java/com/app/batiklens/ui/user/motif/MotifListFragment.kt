package com.app.batiklens.ui.user.motif

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.batiklens.databinding.FragmentMotifListBinding


class MotifListFragment : Fragment() {

    private var _binding: FragmentMotifListBinding? = null
    private val bind get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {

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

}