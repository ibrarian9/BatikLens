package com.app.batiklens.ui.nonUser.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.batiklens.databinding.FragmentOnboardingPageThirdBinding
import com.app.batiklens.ui.nonUser.login.LoginActivity

class OnboardingPageThirdFragment : Fragment() {

    private var _binding: FragmentOnboardingPageThirdBinding? = null
    private val bind get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingPageThirdBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            next.setOnClickListener {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        }
    }
}