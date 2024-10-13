package com.example.mvvmexercise.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmexercise.databinding.ActivityRegisterBinding
import com.example.mvvmexercise.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val role = binding.editTextRole.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && role.isNotEmpty()) {
                viewModel.registerUser(name, email, password, role)
            } else {
                Toast.makeText(this, "Por favor preencha todos os campos.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.registrationStatus.observe(this) { status ->
            when (status) {
                "VerificationEmailSent" -> {
                    Toast.makeText(
                        this,
                        "Registration successful! Verification email sent.",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                "EmailVerificationFailed" -> {
                    Toast.makeText(
                        this,
                        "Registration successful, but failed to send verification email. Please try again later.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                "FirestoreWriteFailed" -> {
                    Toast.makeText(
                        this,
                        "Failed to save user data. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                "RegistrationFailed" -> {
                    Toast.makeText(
                        this,
                        "Registration failed. Please check your input.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                "InvalidEmail" -> {
                    Toast.makeText(
                        this,
                        "Invalid email domain. Please use a valid CESAE email.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}