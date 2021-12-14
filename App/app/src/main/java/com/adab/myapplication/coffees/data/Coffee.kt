package com.adab.myapplication.coffees.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "coffees")
data class Coffee (
    @PrimaryKey @ColumnInfo(name = "_id") var _id: String,
    @ColumnInfo(name = "originName") var originName: String,
    @ColumnInfo(name = "popular") var popular: String,
    @ColumnInfo(name = "roastedDate") var roastedDate: String,
    @ColumnInfo(name = "userId") var userId: String
    ){
    override fun toString(): String = "$originName $popular $roastedDate $_id"
}