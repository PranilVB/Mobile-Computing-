package com.example.smartqueue.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smartqueue.R
import com.example.smartqueue.databinding.FragmentLoginBinding
import com.example.smartqueue.util.Resource
import com.example.smartqueue.util.hide
import com.example.smartqueue.util.show

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString() ?: ""
            viewModel.signIn(email, password)
        }
        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        // Clear error on input change
        binding.etEmail.addTextChangedListener { binding.tvError.hide() }
        binding.etPassword.addTextChangedListener { binding.tvError.hide() }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.btnSignIn.isEnabled = false
                    binding.progress.show()
                    binding.tvError.hide()
                }
                is Resource.Success -> {
                    binding.btnSignIn.isEnabled = true
                    binding.progress.hide()
                    val role = state.data
                    if (role == "admin") {
                        findNavController().navigate(R.id.action_login_to_admin)
                    } else {
                        findNavController().navigate(R.id.action_login_to_home)
                    }
                }
                is Resource.Error -> {
                    binding.btnSignIn.isEnabled = true
                    binding.progress.hide()
                    binding.tvError.text = state.message
                    binding.tvError.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
