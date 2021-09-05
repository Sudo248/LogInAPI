package com.duonglh.retrofitapi.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duonglh.retrofitapi.R
import com.duonglh.retrofitapi.data.Result
import com.duonglh.retrofitapi.databinding.FragmentLogInBinding
import com.duonglh.retrofitapi.di.Injector
import com.google.android.material.snackbar.Snackbar

class LogInFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        Injector.provideMainViewModelFactory(requireContext())
    }

    private lateinit var binding: FragmentLogInBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.statusLogin.observe(viewLifecycleOwner){ status ->
            when(status){
                is Result.Loading -> {
                    // loading here
                }
                is Result.Success -> {
                    findNavController().navigate(R.id.action_logInFragment_to_userFragment)
                }
                is Result.Error -> {
                    binding.loginPassword.error = status.messageException
                }
                else -> {
                    Snackbar.make(view,"Error",500).show()
                }
            }
        }

        binding.apply {
            btnLogin.setOnClickListener {
                val email = loginEmail.editText?.text.toString().trim()
                val password = loginPassword.editText?.text.toString()
                viewModel.logInUser(email, password)
            }
            btnToSignup.setOnClickListener {
                findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
            }
        }
    }

}