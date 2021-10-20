package com.udacity.shoestore.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentLogInBinding

class LogInFragment : Fragment(R.layout.fragment_log_in) {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LogInViewModel by viewModels {
        LogInViewModel.Factory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        _binding?.viewModel = viewModel
        _binding?.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateToWelcomeScreen.observe(viewLifecycleOwner, { event ->
            when (event) {
                is LogInViewModel.Event.Navigate -> {
                    if (event.navigate) {
                        view.findNavController()
                            .navigate(LogInFragmentDirections.actionLogInFragmentToWelcomeFragment())
                        viewModel.clearNavigateData()
                    } else {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                        viewModel.clearNavigateData()
                    }
                }
            }
        })
    }
}