package com.adab.myapplication.coffees.coffees

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adab.myapplication.coffees.data.Coffee
import com.adab.myapplication.coffees.data.CoffeeRepository
import kotlinx.coroutines.launch
import java.util.*
import com.adab.myapplication.core.TAG
import com.adab.myapplication.core.Result


class CoffeeListViewModel : ViewModel() {
    private val mutableItems = MutableLiveData<List<Coffee>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<Coffee>> = mutableItems
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun createItem(position: Int): Unit {
        val list = mutableListOf<Coffee>()
        list.addAll(mutableItems.value!!)
        list.add(Coffee(position.toString(), "Coffee", Date(), true))
        mutableItems.value = list
    }

    fun loadItems() {
        viewModelScope.launch {
            Log.v(TAG, "loadItems...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = CoffeeRepository.loadAll()) {
                is Result.Success -> {
                    Log.d(TAG, "loadItems succeeded");
                    mutableItems.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "loadItems failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}
