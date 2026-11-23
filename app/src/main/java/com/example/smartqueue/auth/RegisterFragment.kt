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
import com.example.smartqueue.databinding.FragmentRegisterBinding
import com.example.smartqueue.util.Resource
import com.example.smartqueue.util.hide
import com.example.smartqueue.util.show

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnCreateAccount.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim() ?: ""
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString() ?: ""
            val confirm = binding.etConfirmPassword.text?.toString() ?: ""
            viewModel.register(name, email, password, confirm)
        }
        binding.btnSignIn.setOnClickListener {
            findNavController().navigateUp()
        }
        listOf(
            binding.etName, binding.etEmail,
            binding.etPassword, binding.etConfirmPassword
        ).forEach { it.addTextChangedListener { binding.tvError.hide() } }
    }

    private fun observeViewModel() {
        viewModel.registerState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.btnCreateAccount.isEnabled = false
                    binding.progress.show()
                    binding.tvError.hide()
                }
                is Resource.Success -> {
                    binding.btnCreateAccount.isEnabled = true
                    binding.progress.hide()
                    findNavController().navigate(R.id.action_register_to_home)
                }
                is Resource.Error -> {
                    binding.btnCreateAccount.isEnabled = true
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
