package com.app.batiklens.ui.user.profil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.batiklens.R
import com.app.batiklens.databinding.FragmentProfileBinding
import com.app.batiklens.ui.nonUser.onboarding.OnboardingActivity
import com.app.batiklens.ui.user.MainActivity
import com.app.batiklens.ui.user.about.AboutActivity
import com.app.batiklens.ui.user.editProfile.EditProfileFragment
import com.app.batiklens.ui.user.history.HistoryFragment
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val bind get() = _binding!!
    private val user = Firebase.auth.currentUser
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {

            user?.let {
                profileViewModel.getDetailProfile(it.uid)
            }

            profileViewModel.detailProfil.observe(viewLifecycleOwner) { data ->
                data?.let {
                    namaLengkap.text = it.name
                    email.text = it.email
                    Glide.with(requireActivity()).load(it.photoUrl).error(R.drawable.baseline_person_24).into(ivPoto)
                }
            }

            editProfile.setOnClickListener {
                (activity as MainActivity).loadFragments(EditProfileFragment())
            }

            bantuan.setOnClickListener {
                startActivity(Intent(requireActivity(), AboutActivity::class.java))
            }

            listHistory.setOnClickListener {
                (activity as MainActivity).loadFragments(HistoryFragment())
            }

            logout.setOnClickListener {
                profileViewModel.logout()
                val i = Intent(requireActivity(), OnboardingActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}