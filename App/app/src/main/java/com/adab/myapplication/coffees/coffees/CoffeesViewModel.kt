package com.adab.myapplication.coffees.coffees

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.adab.myapplication.coffees.data.Coffee
import com.adab.myapplication.coffees.data.CoffeeRepository
import com.adab.myapplication.coffees.data.local.CoffeesDatabase
import kotlinx.coroutines.launch
import com.adab.myapplication.core.TAG
import com.adab.myapplication.core.Result

class CoffeeListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val coffees: LiveData<List<Coffee>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    val coffeeRepository: CoffeeRepository

    init {
        val coffeeDao = CoffeesDatabase.getDatabase(application, viewModelScope).coffeeDao()
        coffeeRepository = CoffeeRepository(coffeeDao)
        coffees = coffeeRepository.coffees
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = coffeeRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}
