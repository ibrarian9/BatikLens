package com.app.batiklens.ui.nonUser.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityRegisterBinding
import com.app.batiklens.di.Injection.messageToast
import com.app.batiklens.ui.nonUser.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var bind: ActivityRegisterBinding
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

            registerViewModel.registerResult.observe(this@RegisterActivity) { result ->
                result.onSuccess {
                    val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)
                }.onFailure {
                    messageToast(this@RegisterActivity, it.message.toString())
                }
            }

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

            masuk.setOnClickListener {

                val dataEmail = email.text.toString().trim()
                val dataPassword = password.text.toString().trim()
                val dataConfirmPass = konfirmasiPassword.text.toString().trim()

                when {
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
                    else -> {
                        registerViewModel.register(dataEmail, dataPassword)
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

    private fun checkData(s: String) {
        messageToast(this@RegisterActivity, s)
    }
}