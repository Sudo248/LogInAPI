package com.duonglh.retrofitapi.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duonglh.retrofitapi.R
import com.duonglh.retrofitapi.data.Error
import com.duonglh.retrofitapi.data.Result
import com.duonglh.retrofitapi.databinding.FragmentLogInBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.Key

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private val viewModel: ShareViewModel by activityViewModels()
    private lateinit var binding: FragmentLogInBinding
    private lateinit var frameLoading: FrameLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        frameLoading = requireActivity().findViewById(R.id.frame_loading)
        with(binding) {
            viewModel.loginWithToken().observe(viewLifecycleOwner){ status->
                when(status){
                    is Result.Loading -> {
                        frameLoading.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        frameLoading.visibility = View.GONE
                        findNavController().navigate(R.id.action_logInFragment_to_userFragment)
                    }
                    else -> {
                        frameLoading.visibility = View.GONE
                    }
                }
            }
            btnLogin.setOnClickListener {
                if(frameLoading.isGone){
                    val email = loginEmail.editText?.text.toString().trim()
                    val password = loginPassword.editText?.text.toString()
                    viewModel.login(email, password).observe(viewLifecycleOwner){ status ->
                        when(status){
                            is Result.Loading -> {
                                frameLoading.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                frameLoading.visibility = View.GONE
                                findNavController().navigate(R.id.action_logInFragment_to_userFragment)
                            }
                            is Result.Error -> {
                                frameLoading.visibility = View.GONE
                                when(status.message){
                                    Error.EMAIl_INVALID -> {
                                        binding.loginEmail.error = "Email invalid"
                                        binding.loginEmail.editText?.text = null
                                        binding.loginPassword.editText?.text = null
                                    }
                                    Error.WRONG_PASSWORD -> {
                                        binding.loginPassword.error = "Wrong Password"
                                        binding.loginPassword.editText?.text = null
                                    }
                                    Error.NOT_FOUND -> {
                                        binding.loginPassword.error = "Not Found"
                                        binding.loginPassword.editText?.text = null
                                    }
                                    else -> {
                                        Snackbar.make(view, "Server Invalid", 2000).show()
                                    }
                                }
                            }
                            else -> {
                                binding.loginPassword.error = "Error occur when login"
                            }
                        }
                    }
                }
            }

            btnToSignup.setOnClickListener {
                findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
            }
            loginEmail.editText?.setOnFocusChangeListener { _, _ ->
                loginPassword.error = null
                loginEmail.error = null
            }
            loginPassword.editText?.setOnFocusChangeListener { _, _ ->
                loginPassword.error = null
            }
        }
    }

}
