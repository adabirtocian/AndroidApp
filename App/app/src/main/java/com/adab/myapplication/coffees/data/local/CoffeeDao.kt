package com.adab.myapplication.coffees.data.local

import androidx.room.*
import androidx.lifecycle.LiveData
import com.adab.myapplication.coffees.data.Coffee

@Dao
interface CoffeeDao {
    @Query("SELECT * FROM coffees ORDER BY originName ASC")
    fun getAll(): LiveData<List<Coffee>>

    @Query("SELECT * FROM coffees WHERE _id=:id ")
    fun getById(id: String?): LiveData<Coffee>

    @Query("SELECT * FROM coffees WHERE _id=:id ")
    fun getByIdCoffee(id: String?): Coffee

    @Query("SELECT * FROM coffees ORDER BY originName ASC")
    fun getAllCoffees(): List<Coffee>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coffee: Coffee)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(coffee: Coffee)

    @Query("DELETE FROM coffees")
    suspend fun deleteAll()

    @Query("DELETE FROM coffees WHERE _id=:id")
    fun deleteById(id:String?)

}