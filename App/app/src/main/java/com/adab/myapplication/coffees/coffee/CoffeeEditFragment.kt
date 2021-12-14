package com.adab.myapplication.coffees.coffee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.adab.myapplication.coffees.data.Coffee
import com.adab.myapplication.core.TAG
import com.adab.myapplication.databinding.FragmentCoffeeEditBinding

class CoffeeEditFragment : Fragment() {
    companion object {
        const val COFFEE_ID = "COFFEE_ID"
    }

    private lateinit var viewModel: CoffeeEditViewModel
    private var coffeeId: String? = null
    private var coffee: Coffee? = null

    private var _binding: FragmentCoffeeEditBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
        arguments?.let {
            if (it.containsKey(COFFEE_ID)) {
                coffeeId = it.getString(COFFEE_ID).toString()
            }
        }
        _binding = FragmentCoffeeEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        setupViewModel()

        binding.fab.setOnClickListener {
            Log.v(TAG, "save coffee " + coffee.toString())
            val c = coffee
            if(c != null) {
                c.originName = binding.itemText.text.toString()
                c.popular = binding.itemPopular.text.toString()
                c.roastedDate = binding.itemDate.text.toString()
                viewModel.saveOrUpdateItem(c)
            }
        }
        binding.itemText.setText(coffeeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i(TAG, "onDestroyView")
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(CoffeeEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            binding.progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })

        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = coffeeId
        if (id == null) {
            coffee = Coffee("-1","","","", "")
        } else {
            viewModel.getCoffeeById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update coffees")
                if( it != null) {
                    coffee = it
                    binding.itemText.setText(it.originName)
                    binding.itemPopular.setText(it.popular)
                    binding.itemDate.setText(it.roastedDate)
                    Log.d("setupViewModel", coffee.toString())
                }
            })
        }
    }
}
