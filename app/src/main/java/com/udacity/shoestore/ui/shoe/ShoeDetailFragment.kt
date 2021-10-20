package com.udacity.shoestore.ui.shoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding

class ShoeDetailFragment : Fragment(R.layout.fragment_shoe_detail) {

    private var _binding: FragmentShoeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoeDetailViewModel by viewModels {
        ShoeDetailViewModel.Factory()
    }

    private val viewModelShared: ShoeListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoeDetailBinding.inflate(inflater, container, false)
        _binding?.viewModel = viewModel
        _binding?.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shoeDetailEvent.observe(viewLifecycleOwner, { event ->
            when (event) {
                ShoeDetailViewModel.Event.Cancel -> {
                    findNavController().popBackStack()
                    viewModel.clearEvent()
                }
                is ShoeDetailViewModel.Event.SaveShoe -> {
                    viewModelShared.addShoe(event.shoe)
                    viewModel.cancel()
                }
            }
        })
    }
}