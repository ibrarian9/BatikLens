package com.app.batiklens.ui.nonUser.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityLoginBinding
import com.app.batiklens.di.Injection.messageToast
import com.app.batiklens.ui.nonUser.register.RegisterActivity
import com.app.batiklens.ui.user.MainActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var bind: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bind.apply {

            SignUpBtn.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            // Observe login result
            loginViewModel.loginResult.observe(this@LoginActivity) { result ->
                result.onSuccess {
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }.onFailure {
                    handleLoginError(it)
                }
            }

            masuk.setOnClickListener {

                val dataEmail = email.text.toString().trim()
                val dataPassword = password.text.toString().trim()

                when {
                    dataEmail.isEmpty() -> {
                        checkData("Harap isi Email")
                        return@setOnClickListener
                    }
                    dataPassword.isEmpty() -> {
                        checkData("Harap isi Password")
                        return@setOnClickListener
                    }
                    else -> {
                        loginViewModel.login(email = dataEmail, password = dataPassword)
                    }
                }
            }
        }
    }

    private fun handleLoginError(exception: Throwable) {
        val errorMessage = when (exception) {
            is FirebaseAuthInvalidUserException -> "Email tidak ditemukan. Silakan daftar terlebih dahulu."
            is FirebaseAuthInvalidCredentialsException -> "Email atau password salah."
            is FirebaseAuthUserCollisionException -> "Akun dengan email ini sudah ada."
            else -> exception.message ?: "Terjadi kesalahan. Silakan coba lagi."
        }
        messageToast(this@LoginActivity, errorMessage)
    }

    private fun checkData(s: String) {
        messageToast(this@LoginActivity, s)
    }
}