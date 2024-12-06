package com.app.batiklens.ui.user.editProfile

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.app.batiklens.R
import com.app.batiklens.databinding.FragmentEditProfileBinding
import com.app.batiklens.di.Injection.getPath
import com.app.batiklens.di.Injection.messageToast
import com.app.batiklens.di.models.DTO.EditProfileDTO
import com.app.batiklens.ui.user.MainActivity
import com.app.batiklens.ui.user.profil.ProfileFragment
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val bind get() = _binding!!
    private val user = Firebase.auth.currentUser
    private var file: File? = null
    private var dataUid: String? = null
    private val editProfileViewModel: EditProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            btnBack.setOnClickListener {
                (activity as MainActivity).loadFragments(ProfileFragment())
            }

            user?.let {
                editProfileViewModel.getDetailProfile(it.uid)
                dataUid = it.uid
            }

            editProfileViewModel.detailProfil.observe(viewLifecycleOwner) { data ->
                data?.let {
                    namaLengkap.text = Editable.Factory.getInstance().newEditable(it.name)
                    email.text = Editable.Factory.getInstance().newEditable(it.email)
                    Glide.with(requireActivity()).load(it.photoUrl).error(R.drawable.baseline_person_24).into(ivPoto)


                }
            }

            editProfileViewModel.editProfile.observe(viewLifecycleOwner) { profil ->
                profil.onSuccess {
                    messageToast(requireActivity(), it)
                    (activity as MainActivity).loadFragments(ProfileFragment())
                }.onFailure {
                    messageToast(requireActivity(), it.toString())
                }
            }

            ivPoto.setOnClickListener {
                resultLauncherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            simpan.setOnClickListener {
                val dataName = namaLengkap.text.toString().trim()
                val dataEmail = email.text.toString().trim()

                when {
                    dataName.isEmpty() -> {
                        checkData("Nama Wajib diisi")
                        return@setOnClickListener
                    }
                    dataEmail.isEmpty() -> {
                        checkData("Email Wajib diisi")
                        return@setOnClickListener
                    }
                    file == null -> {
                        checkData("Foto Profil Wajib diisi")
                        return@setOnClickListener
                    }
                    dataUid == null -> {
                        checkData("UID Wajib diisi")
                        return@setOnClickListener
                    }
                    else -> {
                        val updateProfile = EditProfileDTO(
                            uid = dataUid!!,
                            name = dataName,
                            email = dataEmail,
                            photo = file!!
                        )
                        editProfileViewModel.editDataProfile(updateProfile)
                    }
                }
            }
        }
    }

    private val resultLauncherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            Glide.with(requireActivity()).load(it).fitCenter().into(bind.ivPoto)
            file = getPath(requireActivity(), it)?.let { it1 -> File(it1) }
            if (file == null) {
                messageToast(context = requireActivity(), message = "Failed to get the image file.")
            }
        }
    }

    private fun checkData(s: String) {
        messageToast(requireActivity(), s)
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