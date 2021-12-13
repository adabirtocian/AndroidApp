package com.adab.myapplication.coffees.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.adab.myapplication.coffees.data.Coffee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Coffee::class], version = 1)
abstract class CoffeesDatabase : RoomDatabase() {
    abstract fun coffeeDao(): CoffeeDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeesDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CoffeesDatabase {
            val inst = INSTANCE
            if(inst != null) {
                return inst
            }

            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    CoffeesDatabase::class.java,
                    "coffees_db"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .allowMainThreadQueries()
                    .build()
            INSTANCE = instance
            return instance
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope):
            RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let{database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.coffeeDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(coffeeDao: CoffeeDao) {

        }
    }
}