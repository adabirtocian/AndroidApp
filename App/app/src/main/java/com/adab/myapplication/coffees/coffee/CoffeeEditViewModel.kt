package com.adab.myapplication.coffees.coffee

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

class CoffeeEditViewModel : ViewModel() {
    private val mutableItem = MutableLiveData<Coffee>().apply { value = Coffee("", "", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val item: LiveData<Coffee> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadItem...")
            mutableFetching.value = true
            mutableException.value = null
            when (val result = CoffeeRepository.load(itemId)) {
                is Result.Success -> {
                    Log.d(TAG, "loadItem succeeded");
                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "loadItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableFetching.value = false
        }
    }

    fun saveOrUpdateItem(originName: String, popular: String, roastedDate: String) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            val item = mutableItem.value ?: return@launch
            item.originName = originName
            item.popular = popular
            item.roastedDate = roastedDate
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Coffee>
            if (item._id.isNotEmpty()) {
                result = CoffeeRepository.update(item)
            } else {
                result = CoffeeRepository.save(item)
            }
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}
