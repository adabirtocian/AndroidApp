package com.adab.myapplication.coffees.coffees

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.adab.myapplication.R
import com.adab.myapplication.auth.data.AuthRepository
import com.adab.myapplication.databinding.FragmentCoffeeListBinding
import com.adab.myapplication.core.TAG

class CoffeeListFragment : Fragment() {
    private var _binding: FragmentCoffeeListBinding? = null
    private lateinit var coffeeListAdapter: CoffeeListAdapter
    private lateinit var coffeesModel: CoffeeListViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
        _binding = FragmentCoffeeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        if (!AuthRepository.isLoggedIn) {
            findNavController().navigate(R.id.FragmentLogin)
            return;
        }
        setupItemList()
        binding.fab.setOnClickListener {
            Log.v(TAG, "add new coffee")
            findNavController().navigate(R.id.CoffeeEditFragment)
        }
    }

    private fun setupItemList() {
        coffeeListAdapter = CoffeeListAdapter(this)
        binding.itemList.adapter = coffeeListAdapter
        coffeesModel = ViewModelProvider(this).get(CoffeeListViewModel::class.java)
        coffeesModel.coffees.observe(viewLifecycleOwner, { value ->
            Log.i(TAG, "update coffees")
            coffeeListAdapter.items = value
        })
        coffeesModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        coffeesModel.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        coffeesModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
        _binding = null
    }
}