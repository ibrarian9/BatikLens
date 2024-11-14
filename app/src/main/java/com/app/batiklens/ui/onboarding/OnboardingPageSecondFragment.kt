package com.app.batiklens.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.batiklens.R
import com.app.batiklens.databinding.FragmentOnboardingPageSecondBinding

class OnboardingPageSecondFragment : Fragment() {

    private var _binding: FragmentOnboardingPageSecondBinding? = null
    private val bind get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingPageSecondBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            next.setOnClickListener {
                val thirdFragment = OnboardingPageThirdFragment()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.placeholder, thirdFragment)
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    commit()
                }
            }
        }
    }

}