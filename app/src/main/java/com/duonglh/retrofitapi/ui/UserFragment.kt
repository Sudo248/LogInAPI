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
import com.duonglh.retrofitapi.databinding.FragmentUserBinding
import com.duonglh.retrofitapi.di.Injector

class UserFragment : Fragment() {

    private val viewModel: ShareViewModel by viewModels{
        Injector.provideShareViewModelFactory(requireContext())
    }
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("UserFragment", "Observer user ${viewModel.user.value}")
        viewModel.user.observe(viewLifecycleOwner){
            binding.name.text = it.name
            Log.d("UserFragment", it.toString())
        }

        binding.btnLogout.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_logInFragment)
        }
        binding.btnClear.setOnClickListener {
            viewModel.clearToken()
        }
    }


}