package com.app.batiklens.ui.user.editProfile

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.batiklens.R
import com.app.batiklens.databinding.FragmentEditProfileBinding
import com.app.batiklens.ui.user.MainActivity
import com.app.batiklens.ui.user.profil.ProfileFragment
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val bind get() = _binding!!
    private val user = Firebase.auth.currentUser
    private val editProfileViewModel: EditProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            btnBack.setOnClickListener {
                (activity as MainActivity).loadFragments(ProfileFragment())
            }

            user?.let {
                editProfileViewModel.getDetailProfile(it.uid)
            }

            editProfileViewModel.detailProfil.observe(viewLifecycleOwner) { data ->
                data?.let {
                    namaLengkap.text = Editable.Factory.getInstance().newEditable(it.name)
                    email.text = Editable.Factory.getInstance().newEditable(it.email)
                    Glide.with(requireActivity()).load(it.photoUrl).error(R.drawable.baseline_person_24).into(ivPoto)
                }
            }

            ivPoto.setOnClickListener {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}