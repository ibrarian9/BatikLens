package com.app.batiklens.ui.nonUser.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityRegisterBinding
import com.app.batiklens.di.Injection.getPath
import com.app.batiklens.di.Injection.messageToast
import com.app.batiklens.di.models.dto.RegisterDTO
import com.app.batiklens.ui.nonUser.login.LoginActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class RegisterActivity : AppCompatActivity() {

    private lateinit var bind: ActivityRegisterBinding
    private var file: File? = null
    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bind.apply {

            ivPoto.setOnClickListener {
                resultLauncherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            loginBtn.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            registerViewModel.registerResult.observe(this@RegisterActivity) { result ->
                result.onSuccess {
                    val i = Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(i)
                }.onFailure {
                    messageToast(this@RegisterActivity, it.message ?: "Register Error")
                }
            }

            email.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val isValidEmail: Boolean = !TextUtils.isEmpty(it) && Patterns.EMAIL_ADDRESS.matcher(it).matches()
                        layoutEmail.error = if (isValidEmail){
                            null
                        } else {
                            "Invalid Email format"
                        }
                    }
                }
            })

            password.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    validatePassword()
                }
            })

            konfirmasiPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    validatePassword()
                }
            })

            register.setOnClickListener {

                val dataName = namaLengkap.text.toString().trim()
                val dataEmail = email.text.toString().trim()
                val dataPassword = password.text.toString().trim()
                val dataConfirmPass = konfirmasiPassword.text.toString().trim()

                when {
                    dataName.isEmpty() -> {
                        checkData("Nama Wajib diisi")
                        return@setOnClickListener
                    }
                    dataEmail.isEmpty() -> {
                        checkData("Email Wajib diisi")
                        return@setOnClickListener
                    }
                    dataPassword.isEmpty() -> {
                        checkData("Password Wajib diisi")
                        return@setOnClickListener
                    }
                    dataConfirmPass.isEmpty() -> {
                        checkData("Confirm Password Wajib diisi")
                        return@setOnClickListener
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(dataEmail).matches() -> {
                        checkData("Email format is invalid")
                        return@setOnClickListener
                    }
                    file == null -> {
                        checkData("Foto Profil Wajib Diisi")
                        return@setOnClickListener
                    }
                    else -> {
                        val registerData = RegisterDTO(
                            name = dataName,
                            email = dataEmail,
                            password = dataPassword,
                            confirmPassword = dataConfirmPass
                        )
                        file?.let {
                            val photo = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            val imageMultipart: MultipartBody.Part =
                                MultipartBody.Part.createFormData("profileImage", it.name, photo)
                            registerViewModel.register(registerDTO = registerData, image = imageMultipart )
                        }
                    }
                }
            }
        }
    }

    private fun validatePassword() {
        val dataPass = bind.password.text.toString().trim()
        val dataCPass = bind.konfirmasiPassword.text.toString().trim()

        if (dataCPass.isNotEmpty() && dataPass != dataCPass) {
            bind.passLayout.error = "Passwords do not match"
        } else {
            bind.passLayout.error = null
        }
    }

    private val resultLauncherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            Glide.with(this@RegisterActivity).load(it).fitCenter().into(bind.ivPoto)
            file = getPath(this@RegisterActivity, it)?.let { it1 -> File(it1) }
            if (file == null) {
                messageToast(context = this@RegisterActivity, message = "Failed to get the image file.")
            }
        }
    }

    private fun checkData(s: String) {
        messageToast(this@RegisterActivity, s)
    }
}