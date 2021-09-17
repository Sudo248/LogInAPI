package com.duonglh.retrofitapi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duonglh.retrofitapi.R
import com.duonglh.retrofitapi.data.Error
import com.duonglh.retrofitapi.data.Result
import com.duonglh.retrofitapi.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: ShareViewModel by activityViewModels()
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


        binding.btnSignup.setOnClickListener {
            if (binding.signupProgressBar.isGone) {
                val name = binding.signupName.editText?.text.toString()
                val email = binding.signupEmail.editText?.text.toString()
                val password = binding.signupPassword.editText?.text.toString()
                val confirmPassword = binding.signupConfirmPassword.editText?.text.toString()
                viewModel.register(name, email, password, confirmPassword).observe(viewLifecycleOwner){ status ->
                    when(status){
                        is Result.Loading -> {
                            binding.signupProgressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.signupProgressBar.visibility = View.GONE
                            findNavController().navigate(R.id.action_signUpFragment_to_userFragment)
                        }
                        is Result.Error -> {
                            with(binding){
                                signupProgressBar.visibility = View.GONE
                                clearError()
                                when(status.message){
                                    Error.WRONG_FORMAT_PASSWORD -> {
                                        signupPassword.error = "At least 6 characters"
                                        signupConfirmPassword.editText?.text = null
                                    }
                                    Error.WRONG_PASSWORD -> {
                                        signupConfirmPassword.error = "Wrong Password"
                                    }
                                    Error.EMAIl_INVALID -> {
                                        signupEmail.error = "Email Invalid"
                                        signupPassword.editText?.text = null
                                        signupConfirmPassword.editText?.text = null
                                    }
                                    Error.EMAIL_HAS_USED -> {
                                        signupEmail.error = "Email has been used"
                                        signupPassword.editText?.text = null
                                        signupConfirmPassword.editText?.text = null
                                    }
                                }
                            }
                        }
                        else -> {
                            Snackbar.make(view,"Error",500).show()
                        }
                    }
                }
            }
        }

        with(binding){
            btnToLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
            }
            signupEmail.editText?.setOnFocusChangeListener { _, _ ->
                signupEmail.error = null
            }
            signupPassword.editText?.setOnFocusChangeListener { _, _ ->
                signupPassword.error = null
            }
            signupConfirmPassword.editText?.setOnFocusChangeListener { _, _ ->
                signupConfirmPassword.error = null
            }
        }
    }

    private fun clearError(){
        with(binding){
            signupEmail.error = null
            signupPassword.error = null
            signupConfirmPassword.error = null
        }
    }

}