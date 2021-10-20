package com.udacity.shoestore.ui.shoe

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeListBinding

class ShoeListFragment : Fragment(R.layout.fragment_shoe_list) {
    private var _binding: FragmentShoeListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoeListBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.fabAddShow.setOnClickListener {
            it.findNavController()
                .navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.log_out -> {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle(R.string.app_name)
                    setMessage(R.string.show_list_log_out_message)
                    setPositiveButton(R.string.show_list_log_out_ok) { d, _ ->
                        view?.findNavController()
                            ?.navigate(ShoeListFragmentDirections.actionShoeListFragmentToLogInFragment())
                        d.dismiss()
                    }
                    setNegativeButton(R.string.show_list_log_out_cancel) { d, _ ->
                        d.dismiss()
                    }
                    show()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}