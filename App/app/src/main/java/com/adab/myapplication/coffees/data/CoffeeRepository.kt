package com.adab.myapplication.coffees.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.adab.myapplication.coffees.data.local.CoffeeDao
import com.adab.myapplication.coffees.data.remote.CoffeeApi
import com.adab.myapplication.core.Result
import android.annotation.SuppressLint
import java.lang.Exception
import androidx.work.*

class CoffeeRepository(private val coffeeDao: CoffeeDao) {

    val coffees = coffeeDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        try {
            val coffees = CoffeeApi.service.find()

            for(coffee in coffees) {
                coffeeDao.insert(coffee)
            }
            return Result.Success(true)
        }catch (e: Exception) {
            return Result.Error(e)
        }
    }

    fun getById(coffeeId: String): LiveData<Coffee> {
        return coffeeDao.getById(coffeeId)
    }

    suspend fun save(coffee: Coffee): Result<Coffee> {
        try{
            val createdCoffee = CoffeeApi.service.create(coffee)
            coffeeDao.insert(createdCoffee)
            return Result.Success(createdCoffee)
        } catch(e: Exception){
            Log.d("save","failed to save on server")
            coffeeDao.insert(coffee)
            Log.d("save","saved locally ${coffee._id} ${coffee.originName}")
            saveInBackground(coffee._id)
            Log.d("save","send to be saved on server")
            return Result.Error(e)
        }
    }

    suspend fun update(coffee: Coffee): Result<Coffee> {
        try{
            val updatedCoffee = CoffeeApi.service.update(coffee._id, coffee)
            coffeeDao.update(updatedCoffee)
            return Result.Success(updatedCoffee)

        } catch(e: Exception) {
            Log.d("update","failed to edit on server")
            coffeeDao.update(coffee)
            Log.d("update","edited locally id ${coffee._id}")
            updateInBackground(coffee._id)
            Log.d("update","send to be saved on server")
            return Result.Error(e)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun saveInBackground(coffeeId: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val inputData = Data.Builder()
            .put("coffeeId",coffeeId)
            .build()
        val myWork = OneTimeWorkRequest.Builder(SaveBackground::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance().enqueue(myWork);
    }

    @SuppressLint("RestrictedApi")
    private fun updateInBackground(coffeeId: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val inputData = Data.Builder()
            .put("coffeeId",coffeeId)
            .build()
        val myWork = OneTimeWorkRequest.Builder(UpdateBackground::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance().enqueue(myWork);
    }
}