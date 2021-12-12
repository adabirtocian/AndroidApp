package com.adab.myapplication.coffees.data

import android.util.Log
import com.adab.myapplication.coffees.data.remote.CoffeeApi
import com.adab.myapplication.core.Result
import com.adab.myapplication.core.TAG

object CoffeeRepository {
    private var cachedItems: MutableList<Coffee>? = null;

    suspend fun loadAll(): Result<List<Coffee>> {
        if (cachedItems != null) {
            Log.v(TAG, "loadAll - return cached coffees")
            return Result.Success(cachedItems as List<Coffee>);
        }
        try {
            Log.v(TAG, "loadAll - started")
            val coffees = CoffeeApi.service.find()
            Log.v(TAG, "loadAll - succeeded")
            cachedItems = mutableListOf()
            cachedItems?.addAll(coffees)
            return Result.Success(cachedItems as List<Coffee>)
        } catch (e: Exception) {
            Log.w(TAG, "loadAll - failed", e)
            return Result.Error(e)
        }
    }

    suspend fun load(coffeeId: String): Result<Coffee> {
        val coffee = cachedItems?.find { it._id == coffeeId }
        if (coffee != null) {
            Log.v(TAG, "load - return cached coffee")
            return Result.Success(coffee)
        }
        try {
            Log.v(TAG, "load - started")
            val coffeeRead = CoffeeApi.service.read(coffeeId)
            Log.v(TAG, "load - succeeded")
            return Result.Success(coffeeRead)
        } catch (e: Exception) {
            Log.w(TAG, "load - failed", e)
            return Result.Error(e)
        }
    }

    suspend fun save(coffee: Coffee): Result<Coffee> {
        try {
            Log.v(TAG, "save - started")
            Log.v(TAG, coffee.toString());
            val toAddCoffee = CoffeeWrapper(coffee.originName, coffee.popular, coffee.roastedDate)
            val createdCoffee = CoffeeApi.service.create(toAddCoffee)
            Log.v(TAG, "save - succeeded")
            cachedItems?.add(createdCoffee)
            return Result.Success(createdCoffee)
        } catch (e: Exception) {
            Log.w(TAG, "save - failed", e)
            return Result.Error(e)
        }
    }

    suspend fun update(coffee: Coffee): Result<Coffee> {
        try {
            Log.v(TAG, "update - started")
            val updatedCoffee = CoffeeApi.service.update(coffee._id, coffee)
            val index = cachedItems?.indexOfFirst { it._id == coffee._id }
            if (index != null) {
                cachedItems?.set(index, updatedCoffee)
            }
            Log.v(TAG, "update - succeeded")
            return Result.Success(updatedCoffee)
        } catch (e: Exception) {
            Log.v(TAG, "update - failed")
            return Result.Error(e)
        }
    }
}