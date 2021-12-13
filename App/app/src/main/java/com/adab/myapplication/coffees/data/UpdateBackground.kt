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

class UpdateBackground(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("UpdateBackground","Started")
        val coffeeId = inputData.getString("itemId");
        Log.d("UpdateBackground","CoffeeId: $coffeeId")

        val coffeeDao = CoffeesDatabase.getDatabase(applicationContext, GlobalScope).coffeeDao()
        Log.d("UpdateBackground", "")

        val coffee = coffeeDao.getByIdCoffee(coffeeId)
        Log.d("UpdateBackground", "Returned coffee $coffee")
        if (coffee != null) {
            GlobalScope.launch (Dispatchers.Main) {
                CoffeeApi.service.update(coffee._id, coffee)
            }
            Log.d("UpdateBackground", "Edited coffee $coffee")
            return Result.success();
        }
        return Result.failure();
    }
}