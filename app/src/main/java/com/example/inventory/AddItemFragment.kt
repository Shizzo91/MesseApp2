package com.example.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.Extension.hideKeyboard
import com.example.inventory.data.User
import com.example.inventory.databinding.FragmentAddItemBinding


class AddItemFragment : Fragment() {

    private val itemDetailFragmentArguments: ItemDetailFragmentArgs by navArgs()
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (itemDetailFragmentArguments.itemId > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                currentUser = selectedItem
                bind(currentUser)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory((activity?.application as InventoryApplication).getDatabase.itemDao)
    }

    private fun isUserFromValid(): Boolean {
        return viewModel.isEntryValid(
            binding.userName.text.toString(),
            binding.lastName.text.toString(),
            binding.email.text.toString(),
        )
    }

    /**
     * Binds views with the passed in [user] information.
     */
    private fun bind(user: User) {
        val price = "%.2f".format(user.userEmail)
        binding.apply {
            userName.setText(user.firstname, TextView.BufferType.SPANNABLE)
            lastName.setText(price, TextView.BufferType.SPANNABLE)
            email.setText(user.telephoneNumber.toString(), TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (!isUserFromValid()) {
            Toast.makeText(binding.userName.context, "Das Formular ist nicht valide", Toast.LENGTH_LONG).show()
            return
        }
        viewModel.addNewItem(binding.userName.text.toString(), binding.lastName.text.toString(), binding.email.text.toString(), binding.telephone.text.toString())
        findNavController().navigate(AddItemFragmentDirections.actionAddItemFragmentToItemListFragment())

    }

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (!isUserFromValid()) {
            Toast.makeText(binding.userName.context, "Das Formular konnte nicht aktualisert werden!", Toast.LENGTH_LONG).show()
            return
        }
        viewModel.updateItem(
            this.itemDetailFragmentArguments.itemId,
            this.binding.userName.text.toString(),
            this.binding.lastName.text.toString(),
            this.binding.email.text.toString(),
            this.binding.telephone.text.toString()
        )
        val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
        findNavController().navigate(action)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().currentFocus?.let {
            requireActivity().hideKeyboard(it)
        }
        _binding = null
    }
}
