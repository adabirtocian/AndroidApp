package com.adab.myapplication.coffees.data

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.adab.myapplication.coffees.data.local.CoffeesDatabase
import com.adab.myapplication.coffees.data.remote.CoffeeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaveBackground(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams)  {

    override fun doWork(): Result {
        Log.d("SaveWorker","Started")
        var coffeeId = inputData.getString("coffeeId");
        val coffeeDao = CoffeesDatabase.getDatabase(applicationContext, GlobalScope).coffeeDao()

        val coffees = coffeeDao.getAllCoffees()
        for(coffee in coffees)
        {
            Log.d("SaveWorker",coffee._id+" "+coffee.originName)
        }


        val coffee = coffeeDao.getByIdCoffee(coffeeId)
        coffeeDao.deleteById(coffeeId)
        if (coffee != null) {
            coffee._id=""//(coffeeDao.getSize()+1).toString()
        }
        Log.d("SaveWorker", "")

        Log.d("SaveWorker", "Returned item $coffee")
        if (coffee != null) {
            GlobalScope.launch (Dispatchers.Main) {
                val coffeeWrapper = CoffeeWrapper(coffee.originName, coffee.popular, coffee.roastedDate, coffee.userId)
                val createdItem = CoffeeApi.service.create(coffeeWrapper)
                coffeeDao.insert(createdItem)
            }
            Log.d("SaveWorker", "Saved $coffee")
            return Result.success();
        }
        return Result.failure();
    }
}