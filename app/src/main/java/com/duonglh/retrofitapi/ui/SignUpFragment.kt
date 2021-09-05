package com.duonglh.retrofitapi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duonglh.retrofitapi.R
import com.duonglh.retrofitapi.data.Result
import com.duonglh.retrofitapi.databinding.FragmentSignUpBinding
import com.duonglh.retrofitapi.di.Injector
import com.google.android.material.snackbar.Snackbar


class SignUpFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels{
        Injector.provideMainViewModelFactory(requireContext())
    }
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.statusPost.observe(viewLifecycleOwner){ status ->
            when(status){
                is Result.Loading -> {
                    // add loading pls
                }
                is Result.Success -> {
                    findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
                }
                is Result.Error -> {
                    when(val error = status.messageException){
                        "Email is used" -> binding.signupEmail.error = error
                        "at least 8 characters" -> binding.signupPassword.error = error
                        "Wrong Password" -> binding.signupConfirmPassword.error = error
                        else -> {
                            binding.signupEmail.error = null
                            binding.signupPassword.error = null
                            binding.signupConfirmPassword.error = null
                        }
                    }
                }
                else -> {
                    Snackbar.make(view,"Error",500).show()
                }
            }
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.signupEmail.editText?.text.toString()
            val password = binding.signupPassword.editText?.text.toString()
            val confirmPassword = binding.signupConfirmPassword.editText?.text.toString()
            viewModel.confirmUser(email, password, confirmPassword)
        }

        binding.btnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
        }

    }

}