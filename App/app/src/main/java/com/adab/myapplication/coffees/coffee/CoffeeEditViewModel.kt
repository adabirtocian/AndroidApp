package com.adab.myapplication.coffees.coffee

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.adab.myapplication.coffees.data.Coffee
import com.adab.myapplication.coffees.data.CoffeeRepository
import com.adab.myapplication.coffees.data.local.CoffeesDatabase
import kotlinx.coroutines.launch
import com.adab.myapplication.core.TAG
import com.adab.myapplication.core.Result

class CoffeeEditViewModel(application: Application) : AndroidViewModel(application)  {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val coffeeRepository: CoffeeRepository

    init {
        val coffeeDao = CoffeesDatabase.getDatabase(application, viewModelScope).coffeeDao()
        coffeeRepository = CoffeeRepository((coffeeDao))
    }

    fun getCoffeeById(cofffeId: String): LiveData<Coffee>{
        Log.v(TAG, "getCoffeeById")
        return coffeeRepository.getById(cofffeId)
    }

    fun saveOrUpdateItem(coffee: Coffee) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Coffee>

            if(coffee._id.isNotEmpty()) {
                result = coffeeRepository.update(coffee)
            } else {
                result = coffeeRepository.save(coffee)
            }

            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateCoffee succeeded");
                }

                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateCoffee failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}
