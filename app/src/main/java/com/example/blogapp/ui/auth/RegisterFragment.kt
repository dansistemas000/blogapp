package com.example.blogapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.remote.auth.AuthDataSource
import com.example.blogapp.databinding.FragmentRegisterBinding
import com.example.blogapp.domain.auth.AuthRepoImpl
import com.example.blogapp.presentation.auth.AuthViewModel
import com.example.blogapp.presentation.auth.AuthViewModelFactory


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding : FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel>{
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        getSignUp()
    }

    private fun getSignUp(){


        binding.btnSignup.setOnClickListener {
            val username = binding.editTextUser.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPass.text.toString().trim()
            val confirmPass = binding.txtConfirmPass.text.toString().trim()

            if(password != confirmPass){
                binding.txtConfirmPass.error = "Password does not match"
                binding.editTextPass.error = "Password does not match"
                return@setOnClickListener
            }

            if(username.isEmpty()){
                binding.editTextUser.error = "User is empty"
                return@setOnClickListener
            }

            if(email.isEmpty()){
                binding.editTextEmail.error = "Email is empty"
                return@setOnClickListener
            }

            if(password.isEmpty()){
                binding.editTextPass.error = "Password is empty"
                return@setOnClickListener
            }

            if(confirmPass.isEmpty()){
                binding.txtConfirmPass.error = "confirm Password is empty"
                return@setOnClickListener
            }

            createUser(email,password,username)
        }

    }

    private fun createUser(email: String, password: String, username: String) {
        viewModel.signUp(email,password,username).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = false
                }
                is Resource.Success ->{
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)
                }
                is Resource.Failure->{
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignup.isEnabled = true
                    Toast.makeText(requireContext(),"Error: ${result.exception}", Toast.LENGTH_SHORT).show()
                    Log.d("errorAuth","eror: ${result.exception}")
                }
            }

        })
    }
}